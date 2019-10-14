package generic;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class Simulator {

  public static void setupSimulation(String assemblyProgramFile) {
    int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
    ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
    ParsedProgram.printState();
  }

  public static void assemble(String objectProgramFile) throws IOException {

    // 1. open the objectProgramFile in binary mode
    File file = new File(objectProgramFile);
    FileOutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;

    try {
      outputStream = new FileOutputStream(file);
      dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // 2. write the firstCodeAddress to the file
    dataOutputStream.writeInt(ParsedProgram.getFirstCodeAddress());

    // 3. write the data to the file
    for (int i = 0; i < ParsedProgram.data.size(); i++) {
      dataOutputStream.writeInt(ParsedProgram.data.get(i));
    }

    // 4. assemble one instruction at a time, and write to the file
    int value = 0;
    for (int j = 0; j < ParsedProgram.code.size(); j++) {

      Instruction instruction = ParsedProgram.code.get(j);
      int index = OperationType.valueOf(instruction.getOperationType().toString()).ordinal();

      // for cases 1:22
      if (index >= 0 && index < 22) {

        if (index % 2 == 0) {
          value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 3), 2);
          dataOutputStream.writeInt(value);
        } else {
          value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 2), 2);
          dataOutputStream.writeInt(value);
        }

      } 

      // for cases 22 and 23
      else if (index >= 22 && index < 24) {
        value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 1), 2);
        dataOutputStream.writeInt(value);
      }

      // for case 24
      else if (index == 24) {
        value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 6), 2);
        dataOutputStream.writeInt(value);
      } 

      // for case 25:28
      else if (index > 24 && index < 29) {
        value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 4), 2);
        dataOutputStream.writeInt(value);
      }

      // for case 29
       else if (index == 29) {
        value = Integer.parseUnsignedInt(makeBinaryRep(instruction, index, 5), 2);
        dataOutputStream.writeInt(value);
      }
    }

    // 5. close the file
    dataOutputStream.close();
    outputStream.close();
  }

  // make instruction in binary form
  private static String makeBinaryRep(Instruction instruction, int index, int type) {
    String binaryVal="";
        if (type == 1) {
      if (instruction.destinationOperand.operandType.equals(OperandType.Label)) {
        String label = toBinary2(ParsedProgram.symtab.get(instruction.sourceOperand2.labelValue));
        binaryVal = toBinary(index) 
                    + toBinary(instruction.sourceOperand1.value)
                    + toBinary(instruction.destinationOperand.value) + label;
      } else {
        binaryVal = toBinary(index) 
                    + toBinary(instruction.sourceOperand1.value)
                    + toBinary(instruction.destinationOperand.value)
                    + toBinary2(instruction.sourceOperand2.value);
      }
    } 
    else if (type == 2) {
      
      if (instruction.sourceOperand2.operandType.equals(OperandType.Label)) {
        String label = toBinary2(ParsedProgram.symtab.get(instruction.sourceOperand2.labelValue));
        binaryVal = toBinary(index) + toBinary(instruction.sourceOperand1.value)
            + toBinary(instruction.destinationOperand.value) + label;

      } else {
        binaryVal = toBinary(index) + toBinary(instruction.sourceOperand1.value)
            + toBinary(instruction.destinationOperand.value) + toBinary2(instruction.sourceOperand2.value);
      }

    }
     else if (type == 3) {

      binaryVal = toBinary(index) 
      + toBinary(instruction.sourceOperand1.value) 
      + toBinary(instruction.sourceOperand2.value)
      + toBinary(instruction.destinationOperand.value) 
      + "000000000000";

    }
     else if (type == 4) {

      int diff = ParsedProgram.symtab.get(instruction.destinationOperand.labelValue) - instruction.getProgramCounter();
      if (diff >= 0) {
        binaryVal = toBinary(index) + toBinary(instruction.sourceOperand1.value)
            + toBinary(instruction.sourceOperand2.value) + toBinary2(diff);
      } else {
        diff = Math.abs(diff);
        String binary = twosComplement(toBinary2(diff));
        binaryVal = toBinary(index) + toBinary(instruction.sourceOperand1.value)
            + toBinary(instruction.sourceOperand2.value) + binary;

      }
    }
     else if (type == 5) {
      binaryVal = toBinary4(index);

    }
     else if (type == 6) {

      if (instruction.destinationOperand.operandType.equals(OperandType.Label)) {

        int jumpDiff = ParsedProgram.symtab.get(instruction.destinationOperand.getLabelValue())
            - instruction.getProgramCounter();
        if (jumpDiff >= 0) {

          binaryVal = toBinary(index) + "00000" + toBinary5(instruction.destinationOperand.value);
        } else {
          jumpDiff = Math.abs(jumpDiff);
          String diffBinary = twosComplement(toBinary5(jumpDiff));
          binaryVal = toBinary(index) + "00000" + diffBinary;
        }

      } else {
        binaryVal = toBinary(index) + "00000" + toBinary5(instruction.destinationOperand.value);
      }
    }

    return String.format("%32s", binaryVal).replaceAll(" ", "0");
  }

  // for Opcode, SourceOperand1, SourceOperand2 and Destination Register
  public static String toBinary(int x) {
    return String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
  }

  // for Immediate
  public static String toBinary2(int x) {
    return String.format("%17s", Integer.toBinaryString(x)).replaceAll(" ", "0");
  }

  // For Jump case
  public static String toBinary3(int x) {
    return String.format("%-17s", Integer.toBinaryString(x)).replaceAll(" ", "0");
  }

  // For End case
  public static String toBinary4(int x) {
    return String.format("%-32s", Integer.toBinaryString(x)).replaceAll(" ", "0");
  }

  // For Comparison case
  public static String toBinary5(int x) {
    return String.format("%22s", Integer.toBinaryString(x)).replaceAll(" ", "0");
  }

  // For getting 2's complement
  public static String twosComplement(String bin) {
    String twos = "", ones = "";

    for (int i = 0; i < bin.length(); i++) {
      ones += flip(bin.charAt(i));
    }
    StringBuilder builder = new StringBuilder(ones);
    boolean b = false;
    for (int i = ones.length() - 1; i > 0; i--) {
      if (ones.charAt(i) == '1') {
        builder.setCharAt(i, '0');
      } else {
        builder.setCharAt(i, '1');
        b = true;
        break;
      }
    }
    if (!b)
      builder.append("1", 0, 7);
    twos = builder.toString();

    return twos;
  }

  // Returns '0' for '1' and '1' for '0'
  public static char flip(char c) {
    return (c == '0') ? '1' : '0';
  }
}
