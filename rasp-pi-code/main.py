import base64
import io
import json
from threading import Thread
import tkinter as tk
from time import sleep

import serial
from PIL import Image, ImageTk

pc_connection = None
pong_received = False

root = tk.Tk()


def send_json(msg):
    global pc_connection
    pc_connection.write((json.dumps(msg) + '\r\n').encode('ascii'))
    pc_connection.flushOutput()


def onclick(btn_id):
    send_json({"event": "click", "id": str(btn_id)})


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
    elif event == 'ping':
        send_json({"event": "pong"})
    elif event == 'pi-deck-syn':
        send_json({"event": "pi-deck-syn-ack"})


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
    print('Attempting to Connect')
    ser = serial.Serial('/dev/ttyGS0', 115200, timeout=10)
    global pc_connection
    pc_connection = ser

    send_json({"event": "pi-deck-syn"})
    while True:
        msg = ser.readline()

        if msg is '':
            continue

        try:
            parse_message(json.loads(msg))
        except json.JSONDecodeError:
            print(f"FAIL!")


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
