package core.inventory;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.world.entity.item.WornItem;

public class InventorySlotTarget extends MyDragAndDrop.Target {

    InventorySlot _targetSlot;

    public InventorySlotTarget(InventorySlot actor){
        super(actor);
        _targetSlot = actor;
    }

    @Override
    public boolean drag(MyDragAndDrop.Source source, MyDragAndDrop.Payload payload, float x, float y, int pointer) {
        Gdx.app.log("InventorySlotTarget", "drag");
        return true;
    }

    @Override
    public void reset(MyDragAndDrop.Source source, MyDragAndDrop.Payload payload) {
        Gdx.app.log("InventorySlotTarget", "reset");
    }

    @Override
    public void drop(MyDragAndDrop.Source source, MyDragAndDrop.Payload payload, float x, float y, int pointer) {
        Gdx.app.log("InventorySlotTarget", "drop");
        WornItem sourceActor = (WornItem) payload.getDragActor();
        WornItem targetActor = _targetSlot.getTopInventoryItem();
        InventorySlot sourceSlot = ((InventorySlotSource)source).getSourceSlot();

        if( sourceActor == null ) {
            return;
        }

        //First, does the slot accept the source item type?
        if( !_targetSlot.doesAcceptItemUseType(sourceActor.getSlot()))  {
            //Put item back where it came from, slot doesn't accept item
            sourceSlot.add(sourceActor);
            return;
        }

        if( !_targetSlot.hasItem() ){
            _targetSlot.add(sourceActor);
        }else{
            //If the same item and stackable, add
            if( sourceActor.isSameItemType(targetActor) && sourceActor.isStackable()){
                _targetSlot.add(sourceActor);
            }else{
                //If they aren't the same items or the items aren't stackable, then swap
                InventorySlot.swapSlots(sourceSlot, _targetSlot, sourceActor);
            }
        }
    }

}
