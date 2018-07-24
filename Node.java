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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node<T> {

    private T data = null;

    private List<Node<T>> children = new ArrayList<>();

    private Node<T> parent = null;

    public Node(T data) {
        this.data = data;
    }

    public Node<T> addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node<T>> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public Node<T> copy(){
        Node<T> newNode = new Node<>(data);
        for (Iterator iter = children.iterator(); iter.hasNext();){
            Node<T> child = (Node<T>) iter.next();
            newNode.addChild(child.copy());
        }
        return newNode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public Node getRoot() {
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }

    public void deleteNode() {
        if (parent != null) {
            int index = this.parent.getChildren().indexOf(this);
            this.parent.getChildren().remove(this);
            for (Node<T> each : getChildren()) {
                each.setParent(this.parent);
            }
            this.parent.getChildren().addAll(index, this.getChildren());
        } else {
            deleteRootNode();
        }
        this.getChildren().clear();
    }

    public void deleteNodeWithChildren(){
        if (parent != null) {
            this.parent.getChildren().remove(this);
        } else {
            deleteRootNode();
        }
        this.getChildren().clear();
    }

    public void deleteNodeChildren(){
        this.getChildren().clear();
    }

    public Node<T> deleteRootNode() {
        if (parent != null) {
            throw new IllegalStateException("deleteRootNode not called on root");
        }
        Node<T> newParent = null;
        if (!getChildren().isEmpty()) {
            newParent = getChildren().get(0);
            newParent.setParent(null);
            getChildren().remove(0);
            for (Node<T> each : getChildren()) {
                each.setParent(newParent);
            }
            newParent.getChildren().addAll(getChildren());
        }
        this.getChildren().clear();
        return newParent;
    }

    @Override
    public String toString(){
        String returnString = "";
        if (data instanceof Operation){
            if (((Operation) data).getCurrent_operation() != Operation.OPERATION_EXP && ((Operation) data).getCurrent_operation() != Operation.OPERATION_ADD &&
                    ((Operation) data).getCurrent_operation() != Operation.OPERATION_SUBTRACT){
                returnString = "(";
                for (Node<T> item: children){
                    if (item.getData() instanceof Operation && (((Operation) item.getData()).getCurrent_operation() == Operation.OPERATION_ADD ||
                            ((Operation) item.getData()).getCurrent_operation() == Operation.OPERATION_SUBTRACT)){
                        /*
                        checking to see the children are of addition or subtraction.
                        If so then add parenthesis.
                         */
                        returnString += "(";
                        returnString += item.toString();
                        returnString += ")";
                    }else {
                        returnString += item.toString();
                    }
                    if (((Operation) data).getCurrent_operation() == Operation.OPERATION_DIV){// if this is division we need to check if it is contained in add, sub, or mul
                        if (!item.equals(children.get(children.size() -1))) {// stopping the last slash from showing up
                            returnString += "/";
                        }
                        if (children.size() != 2){
                            throw new IllegalStateException("A division operation must have two children");
                        }

                        if (item.getData() instanceof Operation && ((((Operation) item.getData()).getCurrent_operation() == Operation.OPERATION_EXP) ||
                                (((Operation) item.getData()).getCurrent_operation() == Operation.OPERATION_DIV))){
                            throw new IllegalStateException("Cannot have operation exponent or division as a child under division");
                        }
                    }
                }
                returnString += ")";

            }else if (((Operation) data).getCurrent_operation() == Operation.OPERATION_EXP){

                if (children.size() != 2){
                    throw new IllegalStateException("Operation exponent must have only two children");
                }

                returnString += children.get(0).toString();

                returnString += data.toString() + "(";
                returnString += children.get(1).toString();
                returnString += ")";

            } else{
                for (int i = 0; i < children.size(); i++) {

                    returnString += children.get(i).toString();

                    if (children.get(i).getData() instanceof Function){//gives ending parenthesis for a function
                        returnString += ")";
                    }

                    if (i != children.size() - 1) {// stopping string from adding a unnecessary character
                        returnString += " " + data.toString() + " ";
                    }


                }
            }
        }else{

            returnString += data.toString();

            if (data instanceof Function){// gives begging parenthesis for a function
                returnString += "(";
            }

            if (!children.isEmpty()){// if there are children and the code got to here that means there is a exponent
                for (Node<T> item: children){
                    returnString += item.toString();
                }
            }
        }
        return returnString;
    }
}
