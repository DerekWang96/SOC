.data
str:		 .word 1012,0x11
helloworld0: .ascii "Hello"
			 .space 2
helloworld1: .asciiz "Hello World!"
			 .word 10
helloworld2: .asciiz "Hello World!"
helloworld3: .asciiz "Hello World!"


.text 0x0004
printHello:
	sltu $r2, $r2, $r3
    addi $r2, $r0, 0x4
    div  $r2, $r0
    mtc0  $r2, $r0,2
    lui  $r4, helloworld0
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
    sw $r4, helloworld0( $r2 )

    j 94

.text 0x0124
addtest:
    addi $r1, $r2, 0x10
    sw $r4, str( $r2 )

    j lable

