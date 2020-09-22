import json
import threading
import tkinter as tk
import socket

connected = False
pc_connection = None

root = tk.Tk()

photo = tk.PhotoImage(file="./res/turkeyDerp.png")


def onclick(btn_id):
    print('Connected? ', connected)
    if connected:
        print(btn_id)
        pc_connection.send((json.dumps({"event": "click", "id": str(btn_id)}) + '\r\n').encode('ascii'))


def parse_message(msg):
    print(msg)
    event = msg['event']
    if event == 'set_grid':
        for widget in root.winfo_children():
            widget.destroy()
        for col in range(msg['columns']):
            tk.Grid.columnconfigure(root, col, weight=1)
        for row in range(msg['rows']):
            tk.Grid.rowconfigure(root, row, weight=1)
    elif event == 'set_btn':
        add_button(msg['id'], msg['y'], msg['x'], msg['color'])


def add_button(btn_id, row, col, color):
    btn = tk.Button(root, image=photo, bg=color, command=lambda: onclick(btn_id))
    btn.grid(row=row, column=col, sticky=tk.N + tk.S + tk.E + tk.W, padx=5, pady=5)


root.geometry("800x480")


def start_socket():
    s = socket.socket()
    s.bind(('0.0.0.0', 49494))
    s.listen(5)
    while True:
        print('Waiting for connection...')
        # Establish connection with client.
        c, addr = s.accept()
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

            full_msg += msg

            if b'\n' in full_msg:
                json_msg, full_msg = full_msg.split(b'\n', 1)
                parse_message(json.loads(json_msg))


x = threading.Thread(target=start_socket)
x.start()

root.config(cursor="none")
root.attributes('-fullscreen', True)
root.configure(bg='black')
tk.mainloop()
