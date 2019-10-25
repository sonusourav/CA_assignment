	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 65535, %x9
	store %x5, 0, %x9
	addi %x0, 1, %x7
	beq %x7, %x3, continue
	beq %x3, 1, continue
	add %x0, %x0, %x4
	add %x0, %x0, %x5
	addi %x0, 1, %x8
	addi %x5, 1, %x5
	subi %x3, 1, %x3
fibo:
	add %x4, %x5, %x6
	subi %x9, 1, %x9
	store %x5, 0, %x9
	add %x0, %x5, %x4
	add %x0, %x6, %x5
	beq %x8, %x3, continue
	addi %x8, 1, %x8
	jmp fibo
continue:
	end
