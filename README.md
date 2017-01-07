# USB Mountr
A helper application to set the Mass Storage Device gadget up in Android kernels  
![Screenshot](/screenshot.png?raw=true)

## How it works
Android kernels still include a USB MSD component in their device gadget nowadays, though it is mostly unused since
Android started using MTP. Some OEM ROMs still use it to provide a drivers installation "disc", but it is otherwise
useless.  
This application leverages the module in order to let you use your device as a standard USB thumbdrive for the purpose
of, e.g., booting a distro ISO.

## Building
Standard gradle build process.

## Contributions...
...are welcome, I'm looking for a better icon, and if you feel like implementing it before I do, a menu to create blank
images. Feel free to translate the application to your own language as well.
