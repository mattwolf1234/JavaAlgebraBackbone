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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommonOperations {
    public static int[] convertDecimalToFraction(double x) {
        if (x < 0) {
            int[] temp = convertDecimalToFraction(-x);
            temp[0] = -temp[0];
            return temp;
        }
        double tolerance = 1.0E-6;
        double h1 = 1;
        double h2 = 0;
        double k1 = 0;
        double k2 = 1;
        double b = x;
        do {
            double a = Math.floor(b);
            double aux = h1;
            h1 = a * h1 + h2;
            h2 = aux;
            aux = k1;
            k1 = a * k1 + k2;
            k2 = aux;
            b = 1 / (b - a);
        } while (Math.abs(x - h1 / k1) > x * tolerance);

        return new int[]{(int) h1, (int) k1};
    }

    static public double round(double number, int n){
        String addN = "#.";
        for (int i = 0; i <= n; i++){
            addN += "#";
        }
        DecimalFormat df = new DecimalFormat(addN);
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        return Double.valueOf(df.format(number));
    }

    public static <T> double evaluate(Node<T> list, double[] numbers, Variable[] variables) {
        Node<T> copy = list.copy();
        replaceVariables(copy, numbers, variables);
        simpleSimplify(copy, new ArrayList<>());
        Main.printTree(copy, " ");
        System.out.println("Equation: " + copy);
        return 0;
    }

    private static <T> void replaceVariables(Node<T> list, double[] numbers, Variable[] variables) {
        if (list.getData() instanceof Variable) {
            int index = Arrays.binarySearch(variables, list.getData());
            list.setData((T) new Digit(numbers[index]));
        }
        list.getChildren().forEach(each -> replaceVariables(each, numbers, variables));
    }

    /*
    Simple simplify combines digits. It will execute operations if it is only with digits.
     */
    public static <T> void simpleSimplify(Node<T> list, List<Node<T>> alreadyProcessed) {
        Node<T> temp = null;
        for (Node<T> item : list.getChildren()) {
            if (item.getData() instanceof Operation && !(alreadyProcessed.contains(item))) {
                switch (((Operation) item.getData()).getCurrent_operation()) {
                    case Operation.OPERATION_PAR:
                        // if one of these go deeper, recursive
                        // if not a operation, operation is parent then combine terms if possible.
                        // if there is only one child left then replace parent with child, if not then dont change parent
                        // when done with combining terms then move to parents parent. and ignore this parents optation if it is still around
                        temp = item;
                        break;
                    case Operation.OPERATION_EXP:
                        if (temp != null && (((Operation) temp.getData()).getCurrent_operation() <= Operation.OPERATION_EXP)) {
                            temp = item;
                        }
                        break;
                    case Operation.OPERATION_MUL:
                        if (temp != null && (((Operation) temp.getData()).getCurrent_operation() <= Operation.OPERATION_MUL)) {
                            temp = item;
                        }
                        break;
                    case Operation.OPERATION_DIV:
                        if (temp != null && (((Operation) temp.getData()).getCurrent_operation() <= Operation.OPERATION_DIV)) {
                            temp = item;
                        }
                        break;
                    case Operation.OPERATION_ADD:
                        if (temp != null && (((Operation) temp.getData()).getCurrent_operation() <= Operation.OPERATION_ADD)) {
                            temp = item;
                        }
                        break;
                    case Operation.OPERATION_SUBTRACT:
                        if (temp != null && (((Operation) temp.getData()).getCurrent_operation() <= Operation.OPERATION_SUBTRACT)) {
                            temp = item;
                        }
                        break;
                }
                if (temp == null) {
                    temp = item;
                }
            }else if (item.getData() instanceof Function){
                temp = item;
            }
        }

        if (temp == null) {// no operation was found so it is only digits, variables, and functions left
            // compute numbers finally here
            computeDigits(list);
        } else {
            System.out.println("Low level: " + temp);
            simpleSimplify(temp, new ArrayList<>());
            if (temp.getData() instanceof Operation) {
                alreadyProcessed.add(temp);
            }
            simpleSimplify(list, alreadyProcessed);
        }
    }

    /*
    Argument assumes that it is a parent that is a operation.
    This method performs a operation on its children. It assumes that its children are only digits, variables, or functions
     */
    @SuppressWarnings("unchecked")
    public static <T> void computeDigits(Node<T> list) {
        if (list.getData() instanceof Function){
            if (((Function) list.getData()).isTrigonometry()){ // for now the only function which can be processed is trigonometry
                Digit finalNum = ((Function) list.getData()).inputTrig(list);
                if (finalNum != null){
                    list.setData((T) finalNum);
                    list.deleteNodeChildren();
                }
            }
            return;
        }
        int operation = ((Operation) list.getData()).getCurrent_operation();
        if (operation == Operation.OPERATION_EXP || operation == Operation.OPERATION_DIV) {
            if (list.getChildren().size() == 2) {// only compute if they are two digits
                if (list.getChildren().get(0).getData() instanceof Digit && list.getChildren().get(1).getData() instanceof Digit) {

                    if (operation == Operation.OPERATION_EXP) {
                        list.setData((T) new Digit(Math.pow(((Digit) list.getChildren().get(0).getData()).getDigit(),
                                ((Digit) list.getChildren().get(1).getData()).getDigit()
                        )));
                    }else{// division
                        list.setData((T) new Digit(((Digit) list.getChildren().get(0).getData()).getDigit() /
                                ((Digit) list.getChildren().get(1).getData()).getDigit()));
                    }
                    list.deleteNodeChildren();
                }
            }

        } else { // this handles add, mul, and subtract
            Node<T> newNode = new Node<>((T) new Operation(Operation.OPERATION_ADD));
            Digit finalNum = null;
            for (int i = 0; i < list.getChildren().size(); i++) {

                if (list.getChildren().get(i).getData() instanceof Digit) {// first digit is found, initialize finalNum
                    if (finalNum == null) {
                        finalNum = new Digit(((Digit) list.getChildren().get(i).getData()).getDigit());
                    } else {

                        if (operation == Operation.OPERATION_ADD) {
                            finalNum.addDigit(((Digit) list.getChildren().get(i).getData()));
                        }else if (operation == Operation.OPERATION_SUBTRACT){
                            finalNum.subDigit(((Digit) list.getChildren().get(i).getData()));
                        }else{
                            finalNum.mulDigit(((Digit) list.getChildren().get(i).getData()));
                        }

                    }
                    list.getChildren().get(i).deleteNodeWithChildren();
                    i--;
                } else {

                    newNode.addChild(list.getChildren().get(i).copy());
                }

            }

            if (newNode.getChildren().isEmpty()) {// no children then result is digit
                list.setData((T) finalNum);
                list.deleteNodeChildren();
            } else {

                newNode.addChild(new Node<>((T) finalNum));
                list.deleteNodeChildren();
                list.addChildren(newNode.getChildren());
            }

        }
    }
}
