; 文件名 boot.asm
 
org 7c00h                     ; BIOS读入MBR后，从0x7c00h处开始执行
 
; 下面部分和10h有关中断，10h中断用来显示字符
mov ax, cs
mov es, ax
mov ax, msg
mov bp, ax                    ; ES:BP表示显示字符串的地址
mov cx, msgLen                ; CX存字符长度
mov ax, 1301h                 ; AH=13h表示向TTY显示字符，AL=01h表示显示方式（字符串是否包含显示属性，01h表示不包含）
mov bx, 000fh                 ; BH=00h表示页号，BL=0fh表示颜色
mov dl, 0                     ; 列
int 10h
  
msg: db "hello world, welcome to OS!"
msgLen: equ $ - msg           ; 字符串长度
times 510 - ($ - $$) db 0     ; 填充剩余部分
dw 0aa55h                     ; 魔数，必须有这两个字节BIOS才确认是MBR