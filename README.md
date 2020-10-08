README WIP

Steps for manually setting up:
Must have a way to connect to the pi over wifi/ethernet

1. Flash Raspberry Pi OS Lite onto an SD card
    - Lite because we will manually install the Desktop since we really don't need the full OS 
2. Enable SSH
    - Create a file named `ssh`
    - Also setup wpa_supplicant here if needed
3. Enable Serial over USB
    - In `config.txt` append `dtoverlay=dwc2` to the end
    - In `cmdline.txt` append `modules-load=dwc2,g_serial` on the same line after `rootwait` with a single space between the two.
        - NOTE: if you see something like `quiet init=/usr/lib/raspi-config/init_resize.sh` after `rootwait`, just insert it between the two, but still making sure there is a single space between everything
4. Put the SD card and power up the pi
5. SSH to the pi with `ssh pi@raspberrypi.local`
6. Add Desktop to the OS
    - Just to be safe
        - `sudo apt update`
        - `sudo apt upgrade`
        - `sudo apt dist-upgrade` 
    - Xorg
        - `sudo apt install xserver-xorg`
    - PIXEL Desktop
        - `sudo apt install raspberrypi-ui-mods`
    - LightDM
        - `sudo apt install lightdm`
    - Auto Login
        - `sudo raspi-config`
        - Change `Boot Options -> Desktop / CLI -> Desktop Autologin`
    - Reboot
        - `sudo reboot`
7. Setup HyperPixel screen (If applicable)
    - In a terminal run `sudo curl https://get.pimoroni.com/hyperpixel4 | bash`
    - Reboot
        - `sudo reboot`
    - Rotate Screen If needed
        - `hyperpixel4-rotate right`
        - `sudo reboot`
8. Disable Screen sleep
    - Run `sudo nano /etc/xdg/lxsession/LXDE-pi/autostart`
        - In the file add
        ```
        @xset s noblank
        @xset s off
        @xset -dpms
        ```
    - Reboot again...
        - `sudo reboot`
    
