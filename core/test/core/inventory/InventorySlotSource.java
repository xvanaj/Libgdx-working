package core.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import core.inventory.MyDragAndDrop.Payload;


public class InventorySlotSource extends MyDragAndDrop.Source {

    private MyDragAndDrop _dragAndDrop;
    private InventorySlot _sourceSlot;

    public InventorySlotSource(InventorySlot sourceSlot, MyDragAndDrop dragAndDrop) {
        super(sourceSlot.getTopInventoryItem());
        this._sourceSlot = sourceSlot;
        this._dragAndDrop = dragAndDrop;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        Gdx.app.log("InventorySlotSource", "dragStart");
        Payload payload = new Payload();

        Actor actor = getActor();
        if (actor == null) {
            return null;
        }

        InventorySlot source = (InventorySlot) actor.getParent();
        if (source == null) {
            return null;
        } else {
            _sourceSlot = source;
        }

        _sourceSlot.decrementItemCount(true);

        payload.setDragActor(getActor());
        _dragAndDrop.setDragActorPosition(getActor().getWidth() / 2, -getActor().getHeight() / 2);

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, MyDragAndDrop.Target target) {
        Gdx.app.log("InventorySlotSource", "dragStop");

        if (target == null) {
            _sourceSlot.add(payload.getDragActor());
        }
    }

    public InventorySlot getSourceSlot() {
        return _sourceSlot;
    }
}
