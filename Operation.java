/*
 * Copyright 2018 Matthew Beck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// this class is like a container
public class Operation extends Element{
    public static final int OPERATION_ADD = 1;
    public static final int OPERATION_SUBTRACT = 0;
    public static final int OPERATION_MUL = 3; // multiply
    public static final int OPERATION_DIV = 2;
    public static final int OPERATION_EXP = 4; // exponent
    public static final int OPERATION_PAR = 5; //parenthesis

    private int current_operation;

    public Operation() {
        current_operation = OPERATION_ADD;
    }

    public Operation(int current_operation) {
        this.current_operation = current_operation;
    }

    public int getCurrent_operation() {
        return current_operation;
    }

    public void setCurrent_operation(int current_operation) {
        this.current_operation = current_operation;
    }

    @Override
    public String toString() {
        String returnString;
        switch (current_operation) {
            case OPERATION_ADD:
                returnString = "+";
                break;
            case OPERATION_SUBTRACT:
                returnString = "-";
                break;
            case OPERATION_MUL:
                returnString = "*";
                break;
            case OPERATION_DIV:
                returnString = "/";
                break;
            case OPERATION_EXP:
                returnString = "^";
                break;
            case OPERATION_PAR:
                returnString = "()";
                break;
            default:
                returnString = "";
                break;
        }
        return returnString;
    }
}
