import base64
import io
import json
from threading import Thread
import tkinter as tk
import socket
from time import sleep

from PIL import Image, ImageTk

connected = False
pc_connection = None
pong_received = False

root = tk.Tk()


def onclick(btn_id):
    if connected:
        pc_connection.send((json.dumps({"event": "click", "id": str(btn_id)}) + '\r\n').encode('ascii'))


def clear_screen():
    for widget in root.winfo_children():
        widget.destroy()


def parse_message(msg):
    global pong_received
    event = msg['event']
    if event == 'set_grid':
        clear_screen()
        for col in range(msg['columns']):
            tk.Grid.columnconfigure(root, col, weight=1, uniform="grid_with")
        for row in range(msg['rows']):
            tk.Grid.rowconfigure(root, row, weight=1, uniform="grid_height")
    elif event == 'disconnect':
        clear_screen()
    elif event == 'set_btn':
        add_button(msg['id'], msg['y'], msg['x'], msg['color'], msg['text'], msg['image'])
    elif event == 'pong':
        pong_received = True


def place_image(btn, image):
    msg = base64.b64decode(image)
    buf = io.BytesIO(msg)
    img = Image.open(buf)
    img = img.resize((btn.winfo_width(), btn.winfo_height()), Image.ANTIALIAS)
    photo_img = ImageTk.PhotoImage(img)
    # Why do we set it twice???? IDK, but it doesn't work if we don't 🙃
    btn.configure(image=photo_img)
    btn.image = photo_img


def add_button(btn_id, row, col, color, text, image):
    btn = tk.Button(root, bg=color, activebackground=color, borderwidth=0, command=lambda: onclick(btn_id))
    if image:
        # Hack to wait for the button size to get applied... IDK what the "right" way is
        btn.after(200, lambda: place_image(btn, image))

    else:
        btn['text'] = text
    btn.grid(row=row, column=col, sticky=tk.NSEW, padx=5, pady=5)
    btn.update()


root.geometry("800x480")


def start_socket():
    s = socket.socket()
    s.bind(('0.0.0.0', 49494))
    s.listen(5)
    while True:
        print('Waiting for connection...')
        # Establish connection with client.
        c, addr = s.accept()
        c.settimeout(5)
        print('Got connection from', addr)
        global connected, pc_connection
        connected = True
        pc_connection = c

        full_msg = b''
        while connected:
            msg = b''

            try:
                msg = c.recv(16)
            except ConnectionResetError:
                connected = False
            except socket.timeout:
                continue

            full_msg += msg

            if b'\n' in full_msg:
                json_msg, full_msg = full_msg.split(b'\n', 1)
                parse_message(json.loads(json_msg))

        clear_screen()


def start_ping_thread():
    global connected, pong_received, pc_connection

    fails = 0

    while True:
        if connected:
            try:
                print((json.dumps({"event": "ping"}) + '\r\n').encode('ascii'))
                pc_connection.send((json.dumps({"event": "ping"}) + '\r\n').encode('ascii'))
                sleep(5)
                if pong_received:
                    pong_received = False
                    fails = 0
                else:
                    fails += 1
                    if fails == 3:
                        connected = False
            except BrokenPipeError:
                connected = False
                fails = 0
        sleep(5)


x = Thread(target=start_socket)
x.start()
# TODO needs to be made thread safe
ping_thread = Thread(target=start_ping_thread)
ping_thread.start()

root.config(cursor="none")
root.attributes('-fullscreen', True)
root.configure(bg='black')
tk.mainloop()
