.data
str:		 .word 10
helloworld0: .ascii "Hello World!"
			 .space 2
helloworld1: .asciiz "Hello World!"
			 .word 10
helloworld2: .asciiz "Hello World!"
helloworld3: .asciiz "Hello World!"

.data
helloworld: .asciiz "Hello World!"

.text 0x0023
printHello:
	addu $r2, $r2, $r3
    addi $r2, $r0, 0x4
    div  $r2, $r0
    sll  $r2, $r0,10
    lui  $r4, helloworld
    ori  $r4, $r4, 20
    nop
    syscall
    j lable

main:
    j printHello
exit:
    jr $r5
    eret

.text
lable:

    addi $r1, $r2, 0x10 #454234343
    sw $r4, str( $r2 )

    j 94

