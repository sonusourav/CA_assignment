	.data
a:
	10
	.text
main:
load %x0, $a, %x3
add %x0, %x3, %x4
add %x0, %x0, %x5
check:
beq %x4, %x0, continue
muli %x5, 10, %x5
divi %x4, 10, %x6
add %x5, %x31, %x5
add %x0, %x6, %x4
jmp check
continue:
beq %x3, %x5, endl
subi %x0, 1, %x10
end
endl:
addi %x0, 1, %x10
end
