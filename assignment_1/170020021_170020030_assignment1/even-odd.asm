.data
a:
0
.text
main:
load %x0, $a, %x4
add %x0, %x0, %x3
check:
beq %x3, %x4, continue
bgt %x3, %x4, great
addi %x3, 2, %x3
jmp check
continue:
subi %x0, 1, %x10
end
great:
addi %x0, 1, %x10
end
