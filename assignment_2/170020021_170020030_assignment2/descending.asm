	.data
a:
	70
n:
	1
	.text
main:
load %x0, $n, %x3
addi %x0, 1, %x10
beq %x10, %x3, onenumber
subi %x3, 1, %x3
subi %x3, 1, %x4
check:
add %x0, %x0, %x5
jmp continue
conafter:
subi %x3, 1, %x3
beq %x0, %x3, endl
jmp check
continue:
load %x5, $a, %x6
addi %x5, 1, %x7
load %x7, $a, %x8
bgt %x8, %x6, swap
swapafter:
beq %x5, %x4, conafter
addi %x5, 1, %x5
jmp continue
swap:
store %x8, $a, %x5
store %x6, $a, %x7
jmp swapafter
endl:
end
onenumber:
end
