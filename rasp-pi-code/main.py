import base64
import json
from threading import Thread
import tkinter as tk
from time import sleep

import serial
from PIL import Image, ImageTk

pc_connection = None
connected = False
pong_received = False

button_map = {}

root = tk.Tk()
frame = tk.Frame(root, width=800, height=480, bg='black')
frame.pack(fill=tk.BOTH, expand=1)


def send_json(msg):
    global pc_connection, connected
    if pc_connection is not None:
        try:
            pc_connection.write((json.dumps(msg) + '\r\n').encode('ascii'))
            pc_connection.flushOutput()
        except OSError:
            connected = False


def onclick(btn_id):
    send_json({"event": "click", "id": str(btn_id)})


def clear_screen():
    global frame, button_map
    button_map.clear()
    frame.destroy()
    frame = tk.Frame(root, width=800, height=480, bg='black')
    frame.pack(fill=tk.BOTH, expand=1)


def parse_message(msg):
    global pong_received
    event = msg['event']
    if event == 'set_grid':
        clear_screen()
        for col in range(msg['columns']):
            tk.Grid.columnconfigure(frame, col, weight=1, uniform="grid_with")
        for row in range(msg['rows']):
            tk.Grid.rowconfigure(frame, row, weight=1, uniform="grid_height")
    elif event == 'disconnect':
        clear_screen()
    elif event == 'set_btn':
        add_button(msg['id'], msg['y'], msg['x'], msg['color'], msg['text'], msg['image'])
    elif event == 'add_img':
        add_img(msg['id'], msg['image'])
    elif event == 'respond_img':
        add_img(msg['id'], msg['image'])
        place_image(msg['btn_id'], button_map[msg['btn_id']], msg['id'], True)
    elif event == 'pong':
        pong_received = True
    elif event == 'ping':
        send_json({"event": "pong"})
    elif event == 'pi-deck-syn':
        send_json({"event": "pi-deck-syn-ack"})


# Recurse is used here to prevent an infinite loop
def place_image(btn_id, btn, image, recurse=False):
    try:
        img = Image.open("images/" + image)
    except FileNotFoundError:
        if not recurse:
            send_json({"event": "request_img", "id": image, "btn_id": btn_id})
        return

    img = img.resize((btn.winfo_width(), btn.winfo_height()), Image.ANTIALIAS)
    photo_img = ImageTk.PhotoImage(img)
    # Why do we set it twice???? IDK, but it doesn't work if we don't 🙃
    btn.configure(image=photo_img)
    btn.image = photo_img


def add_button(btn_id, row, col, color, text, image):
    global button_map
    btn = tk.Button(frame, bg=color, activebackground=color, borderwidth=0, command=lambda: onclick(btn_id))
    button_map[btn_id] = btn
    if image:
        # Hack to wait for the button size to get applied... IDK what the "right" way is
        btn.after(200, lambda: place_image(btn_id, btn, image))
    else:
        btn['text'] = text
    btn.grid(row=row, column=col, sticky=tk.NSEW, padx=5, pady=5)
    btn.update()


def add_img(image_name, image_enc):
    img = base64.b64decode(image_enc)
    with open("images/" + image_name, "wb") as text_file:
        text_file.write(img)


root.geometry("800x480")


def start_socket():
    print('Attempting to Connect')
    global pc_connection, connected

    ser = serial.Serial('/dev/ttyGS0', 115200, timeout=10)
    pc_connection = ser
    connected = True

    while True:
        send_json({"event": "pi-deck-syn"})
        while connected:
            msg = ser.readline()

            if msg is '':
                continue

            try:
                parse_message(json.loads(msg))
            except json.JSONDecodeError:
                print(f"FAIL!")
        pc_connection.close()
        pc_connection.open()
        connected = True


def start_ping_thread():
    global pong_received

    fails = 0

    while True:
        send_json({"event": "ping"})
        sleep(5)
        if pong_received:
            pong_received = False
            fails = 0
        else:
            fails += 1
            if fails == 3:
                clear_screen()


x = Thread(target=start_socket)
x.start()
ping_thread = Thread(target=start_ping_thread)
ping_thread.start()

root.config(cursor="none")
root.attributes('-fullscreen', True)
root.configure(bg='black')
tk.mainloop()
