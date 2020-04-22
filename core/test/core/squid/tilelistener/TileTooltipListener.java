package core.squid.tilelistener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import core.cartezza.mapgenerator.generator.domain.Tile;

public class TileTooltipListener extends InputListener {

    private TileTooltip _toolTip;
    private boolean _isInside = false;
    private Vector2 _currentCoords;
    private Vector2 _offset;

    public TileTooltipListener(TileTooltip toolTip){
        this._toolTip = toolTip;
        this._currentCoords = new Vector2(0,0);
        this._offset = new Vector2(20, 10);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y){
        Tile tile = (Tile)event.getListenerActor();

        if( _isInside ){
            //_currentCoords.set(x, y);
            //tile.localToStageCoordinates(_currentCoords);
            _toolTip.toFront();
        }
        return false;
    }


    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        Tile tile = (Tile)event.getListenerActor();
        _toolTip.setVisible(tile, false);
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
        Tile tile = (Tile)event.getListenerActor();

        _isInside = true;

        _currentCoords.set(x, y);
        //tile.localToStageCoordinates(_currentCoords);

        _toolTip.updateDescription(tile);
        //_toolTip.setPosition(_currentCoords.x + _offset.x, _currentCoords.y + _offset.y);
        _toolTip.setPosition(tile.x*16 + _offset.x, tile.y *16 + _offset.y);
        _toolTip.toFront();
        _toolTip.setVisible(tile, true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
        Tile tile = (Tile) event.getListenerActor();
        _toolTip.setVisible(tile, false);
        _isInside = false;

        _currentCoords.set(x, y);
        tile.localToStageCoordinates(_currentCoords);
    }

}
