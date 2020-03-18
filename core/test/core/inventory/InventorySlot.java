package core.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.mygdx.game.asset.Utility;
import com.mygdx.game.world.entity.item.WornItem;
import com.mygdx.game.world.enums.item.EquipmentSlot;

public class InventorySlot extends Stack implements InventorySlotSubject {

    //All slots have this default image
    private Stack _defaultBackground;
    private Image _customBackgroundDecal;
    private Label _numItemsLabel;
    private int _numItemsVal = 0;
    private EquipmentSlot _filterItemType;

    private Array<InventorySlotObserver> _observers;

    public InventorySlot(){
        _filterItemType = null; //filter nothing
        _defaultBackground = new Stack();
        _customBackgroundDecal = new Image();
        _observers = new Array<InventorySlotObserver>();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog")));

        _numItemsLabel = new Label(String.valueOf(_numItemsVal), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        _numItemsLabel.setAlignment(Align.bottomRight);
        _numItemsLabel.setVisible(false);

        _defaultBackground.add(image);

        _defaultBackground.setName("background");
        _numItemsLabel.setName("numitems");

        this.add(_defaultBackground);
        this.add(_numItemsLabel);
    }

    public InventorySlot(EquipmentSlot filterItemType, Image customBackgroundDecal){
        this();
        _filterItemType = filterItemType;
        _customBackgroundDecal = customBackgroundDecal;
        _defaultBackground.add(_customBackgroundDecal);
    }

    public void decrementItemCount(boolean sendRemoveNotification) {
        _numItemsVal--;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if( _defaultBackground.getChildren().size == 1 ){
            _defaultBackground.add(_customBackgroundDecal);
        }
        checkVisibilityOfItemCount();
        if( sendRemoveNotification ){
            notify(this, InventorySlotObserver.SlotEvent.REMOVED_ITEM);
        }

    }

    public void incrementItemCount(boolean sendAddNotification) {
        _numItemsVal++;
        _numItemsLabel.setText(String.valueOf(_numItemsVal));
        if( _defaultBackground.getChildren().size > 1 ){
            _defaultBackground.getChildren().pop();
        }
        checkVisibilityOfItemCount();
        if( sendAddNotification ){
            notify(this, InventorySlotObserver.SlotEvent.ADDED_ITEM);
        }
    }

    @Override
    public void add(Actor actor) {
        Gdx.app.log("InventorySlot", "add " + actor.getName());

        super.add(actor);

        if( _numItemsLabel == null ){
            return;
        }

        if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
            incrementItemCount(true);
        }
    }

    public void remove(Actor actor) {
        Gdx.app.log("InventorySlot", "remove " + actor.getName());

        super.removeActor(actor);

        if( _numItemsLabel == null ){
            return;
        }

        if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
            decrementItemCount(true);
        }
    }

    public void add(Array<Actor> array) {
        for( Actor actor : array){
            super.add(actor);

            if( _numItemsLabel == null ){
                return;
            }

            if( !actor.equals(_defaultBackground) && !actor.equals(_numItemsLabel) ) {
                incrementItemCount(true);
            }
        }
    }

    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  arrayChildren.size - 2;
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(true);
                items.add(arrayChildren.pop());
            }
        }
        return items;
    }

    public void updateAllInventoryItemNames(String name){
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for(int i = arrayChildren.size - 1; i > 1 ; i--) {
                arrayChildren.get(i).setName(name);
            }
        }
    }

    public void removeAllInventoryItemsWithName(String name){
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for(int i = arrayChildren.size - 1; i > 1 ; i--) {
                String itemName = arrayChildren.get(i).getName();
                if( itemName.equalsIgnoreCase(name)){
                    decrementItemCount(true);
                    arrayChildren.removeIndex(i);
                }
            }
        }
    }


    public void clearAllInventoryItems(boolean sendRemoveNotifications) {
        if( hasItem() ){
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems =  getNumItems();
            for(int i = 0; i < numInventoryItems; i++) {
                decrementItemCount(sendRemoveNotifications);
                arrayChildren.pop();
            }
        }
    }

    private void checkVisibilityOfItemCount(){
        if( _numItemsVal < 1){
            _numItemsLabel.setVisible(false);
        }else{
            _numItemsLabel.setVisible(true);
        }
    }

    public boolean hasItem(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                return true;
            }
        }
        return false;
    }

    public int getNumItems(){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 2;
        }
        return 0;
    }

    public int getNumItems(String name){
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            int totalFilteredSize = 0;
            for( Actor actor: items ){
                if( actor.getName().equalsIgnoreCase(name)){
                    totalFilteredSize++;
                }
            }
            return totalFilteredSize;
        }
        return 0;
    }

    public boolean doesAcceptItemUseType(EquipmentSlot itemUseType){
        return _filterItemType == null || _filterItemType.equals(itemUseType);
    }

    public WornItem getTopInventoryItem(){
        WornItem actor = null;
        if( hasChildren() ){
            SnapshotArray<Actor> items = this.getChildren();
            if( items.size > 2 ){
                actor = (WornItem) items.peek();
            }
        }
        return actor;
    }

    static public void swapSlots(InventorySlot inventorySlotSource, InventorySlot inventorySlotTarget, WornItem dragActor){
        Gdx.app.log("InventorySlot", "swapSlots");
        //check if items can accept each other, otherwise, no swap
        if( !inventorySlotTarget.doesAcceptItemUseType(dragActor.getSlot()) ||
                !inventorySlotSource.doesAcceptItemUseType(inventorySlotTarget.getTopInventoryItem().getSlot())) {
            inventorySlotSource.add(dragActor);
            return;
        }

        //swap
        Array<Actor> tempArray = inventorySlotSource.getAllInventoryItems();
        tempArray.add(dragActor);
        inventorySlotSource.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }

    @Override
    public void addObserver(InventorySlotObserver slotObserver) {
        _observers.add(slotObserver);
    }

    @Override
    public void removeObserver(InventorySlotObserver slotObserver) {
        _observers.removeValue(slotObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for(InventorySlotObserver observer: _observers){
            _observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event) {
        for(InventorySlotObserver observer: _observers){
            observer.onNotify(slot, event);
        }
    }
}
