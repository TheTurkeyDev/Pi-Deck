README WIP

1. Enable SSH over USB
    - Add a file named `ssh`
    - In `config.txt` append `dtoverlay=dwc2` to the end
    - In `cmdline.txt` append `modules-load=dwc2,g_ether` on the same line after `rootwait` with a single space between the two.
        - NOTE: if you see something like `quiet init=/usr/lib/raspi-config/init_resize.sh` after `rootwait`, just insert it between the two, but still making sure there is a single space between everything
2. Put the SD card and power up the pi
3. SSH to the pi with `ssh pi@raspberrypi.local`
4 In a terminal
    - Run `sudo nano /etc/xdg/lxsession/LXDE-pi/autostart`
        - In the file add
        ```
        @xset s off
        @xset -dpms
        ```
    
