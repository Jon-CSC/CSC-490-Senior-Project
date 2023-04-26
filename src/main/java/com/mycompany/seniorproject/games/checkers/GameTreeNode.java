/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.checkers;

import java.util.ArrayList;
import java.util.List;

public class GameTreeNode {
    private List<GameTreeNode> children = new ArrayList<>();
    private GameTreeNode parent = null;
    private BoardLogic data = null;

    public GameTreeNode(BoardLogic rootData) {
        data = rootData;
    }

    public BoardLogic getData() {
        return data;
    }

    public List<GameTreeNode> getChildren() {
        return children;
    }

    public void addChild(GameTreeNode newChild) {
        children.add(newChild);
    }
}
