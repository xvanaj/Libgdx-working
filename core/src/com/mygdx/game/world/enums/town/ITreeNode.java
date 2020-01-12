package com.mygdx.game.world.enums.town;

import java.util.Set;

public interface ITreeNode extends IIconified {

    <T extends ITreeNode> Set<ITreeNode> getPrerequisities();

    <T extends ITreeNode> void setPrerequisities(ITreeNode... prerequisities);

    float getCost();
}
