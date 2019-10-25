	.data
a:
	10
	.text
main:
load %x0, $a, %x3
addi %x0, 1, %x4
subi %x3, 1, %x5
prime:
beq %x4, %x5, continue
addi %x4, 1, %x4
div %x3, %x4, %x7
mul %x7, %x4, %x6
sub %x3, %x6, %x6
beq %x6, %x0, success
jmp prime
success:
subi %x0, 1, %x10
end
continue:
addi %x0, 1, %x10
end
