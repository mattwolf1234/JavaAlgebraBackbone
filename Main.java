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

public class Main {

    public static void main(String[] args) {
        Node<Digit> root = new Node<>(new Digit(10));

        Node<Digit> node1 = root.addChild(new Node<>(new Digit(9)));
        Node<Digit> node2 = node1.addChild(new Node<>(new Digit(8)));
        Node<Digit> node3 = root.addChild(new Node<>(new Digit(7)));
        root.deleteRootNode();

        printTree(root, " ");
    }

    private static <T> void printTree(Node<T> node, String appender) {
        System.out.println(appender + node.getData());
        node.getChildren().forEach(each ->  printTree(each, appender + appender));
    }
}
