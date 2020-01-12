package com.mygdx.game.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.gdxutils.MyTmxMapLoader;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GameAssetManager extends AssetManager {

    private static final String TAG = GameAssetManager.class.getName();
    private static Date subDate = new Date();
    private static Map<String, TextFileContent> txtFilesLines = new HashMap();
    private Array<AssetDescriptor> assets = new Array<AssetDescriptor>();

    public GameAssetManager(final InternalFileHandleResolver internalFileHandleResolver) {
        super(internalFileHandleResolver);

        setLoader(TiledMap.class, new MyTmxMapLoader());
    }

    public void loadImages() {
        subDate = new Date();
        loadAssetsFromFolder("portrait", Texture.class, null);
        loadAssetsFromFolder("texture/button", Texture.class, null);
        loadAssetsFromFolder("texture/button/menu", Texture.class, null);
        loadAssetsFromFolder("texture/icon", Texture.class, null);
        finishLoading();
        logLoading("Image assets loaded");
        //loadAssetsFromFolder("images", Pixmap.class,"png");
        //loadAssetsFromFolder("texture", Texture.class,null);
        //load(lazorPack, TextureAtlas.class);
    }

    private static void logLoading(final String s) {
        Gdx.app.log(TAG, s + " in " + (new Date().getTime() - subDate.getTime()) + " millis");
    }

    public void loadSounds() {
        subDate = new Date();
        //load(explosionSound, Sound.class);
        logLoading("Sound assets loaded");
    }

    public void loadFonts() {
        subDate = new Date();
        loadAssetsFromFolder("font", BitmapFont.class, "fnt");
        finishLoading();
        logLoading("Font assets loaded");
    }

    public void loadParticleEffects() {
        subDate = new Date();
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        pep.atlasFile = "images/images.pack";
        //load("particles/sparks.pe", ParticleEffect.class, pep);
        finishLoading();
        logLoading("Particle assets loaded");
    }

    public void loadTextures() {
        subDate = new Date();
        load("core/assets/texture/background.jpg", Texture.class);
        load("core/assets/images/button/button.png", Texture.class);
        load("core/assets/images/button/button-disabled.png", Texture.class);
        load("core/assets/images/button/button-disabled.png", Texture.class);
        load("core/assets/texture/button/BTNSphere_Of_Gods.png", Texture.class);
        finishLoading();
        logLoading("Texture assets loaded");
    }

    public void loadSkin() {
        subDate = new Date();
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter("uiskin.atlas");
        load("uiskin.json", Skin.class, params);
        load("core/assets/skin/pixthulhu/pixthulhu-ui.json", Skin.class);
        finishLoading();
        logLoading("Skin assets loaded");
    }

    public static void loadTextFiles() {
        subDate = new Date();
        loadTextFile("elders", "core/assets/names/elders.txt");
        loadTextFile("hero", "core/assets/names/hero.txt");
        loadTextFile("meleeWeaponNames", "core/assets/names/meleeWeaponNames.txt");
        loadTextFile("currency", "core/assets/names/currency.txt");
        loadTextFile("rangedWeaponNames", "core/assets/names/rangedWeaponNames.txt");
        loadTextFile("spellBookNames", "core/assets/names/spellBookNames.txt");
        loadTextFile("towns", "core/assets/names/towns.txt");
        loadTextFile("resource", "core/assets/names/resource.txt");
        loadTextFile("spellResource", "core/assets/names/spellResource.txt");
        loadTextFile("world", "core/assets/names/world.txt");
        loadTextFile("monster", "core/assets/names/monster.txt");

        logLoading("Textfile assets loaded");
    }

    private static void loadTextFile(final String key, final String path) {

        FileHandle internal = Gdx.files.classpath(path);
        TextFileContent textFileContent = new TextFileContent();
        if (!internal.file().exists()) {
            internal = Gdx.files.classpath(path.replace("core/", ""));
        }

        try {
            InputStream in = new FileInputStream(internal.file());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String line = r.readLine();
            final String[] firstLineParts = line.split(";");
            Arrays.stream(firstLineParts).forEach(part -> textFileContent.getAvailableKeys().add(part));

            while ((line=r.readLine()) != null) {
                final TextFileContent.TextFileLine textFileLine = new TextFileContent.TextFileLine();
                String[] parts = line.split(";");
                if (parts.length != firstLineParts.length) {
                    Gdx.app.log("TextFileLoader", "File with path \"" + path + "\" contains incorrect number of values in line " + line);
                }
                for (int i = 0; i < firstLineParts.length; i++) {
                    textFileLine.getValues().put(TextFileContent.FileKeyType.valueOf(firstLineParts[i].toUpperCase()), parts[i]);
                }
                textFileContent.getLines().add(textFileLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtFilesLines.put(key, textFileContent);
    }

    public static TextFileContent getTextFileContentsStatic(String key) {
        if (txtFilesLines.get(key) == null) {
            loadTextFile(key, "core/assets/names/" + key + ".txt");
        }
        return txtFilesLines.get(key);
    }

    public TextFileContent getTextFileContents(String key) {
        if (txtFilesLines.get(key) == null) {
            loadTextFiles();
        }
        return txtFilesLines.get(key);
    }

    public void loadAssetsFromFolder(final String folder, final Class clazz, final String extension){
        assets = new Array<AssetDescriptor>();
        assets.add(new AssetDescriptor(folder, clazz, extension));

        loadPreparedAssets();
    }

    //for tests?
    public void loadAll(){
        assets.add(new AssetDescriptor("music", Music.class, null));
        assets.add(new AssetDescriptor("sound", Sound.class, null)); // You could remove all but this one
        //assets.add(new AssetDescriptor("skin", Skin.class));
        assets.add(new AssetDescriptor("texture", Texture.class, null));
        assets.add(new AssetDescriptor("atlas", TextureAtlas.class, null));
        assets.add(new AssetDescriptor("font", BitmapFont.class, "fnt"));
        assets.add(new AssetDescriptor("freetype", FreeTypeFontGenerator.class, null));
        assets.add(new AssetDescriptor("effect", ParticleEffect.class, null));
        assets.add(new AssetDescriptor("portrait", Texture.class,null));
        assets.add(new AssetDescriptor("images", Pixmap.class,"png"));
        assets.add(new AssetDescriptor("texture/button", Texture.class, null));
        assets.add(new AssetDescriptor("texture/icon", Texture.class,null));
        assets.add(new AssetDescriptor("region", PolygonRegion.class, null));
        assets.add(new AssetDescriptor("model", Model.class, null));
        assets.add(new AssetDescriptor("maps", TiledMap.class, "tmx"));

        loadPreparedAssets();
        logLoading("All assets loaded");
    }

    public static class AssetDescriptor {
        private String folder;
        private Class<?> assetType;
        private String extension;

        public AssetDescriptor(String folder, Class<?> assetType, String extension) {
            this.folder = folder;
            this.assetType = assetType;
            this.extension = extension;
        }
    }

    public void loadPreparedAssets() {
        for (AssetDescriptor descriptor : assets) {
            FileHandle folder = this.getFileHandleResolver().resolve("core/assets").child(descriptor.folder);
            if (!folder.exists()) {
                folder = this.getFileHandleResolver().resolve("assets").child(descriptor.folder);
                if (!folder.exists()) {
                    continue;
                }
            }

            for (FileHandle asset : folder.list()) {
                if (!asset.isDirectory() && (descriptor.extension == null || descriptor.extension == "" || asset.extension().equals(descriptor.extension))) {
                    load(asset.path(), descriptor.assetType);
                    finishLoadingAsset(asset.path());
                }
            }
        }
    }

    public List<Texture> getAllPortraits() {
        final Array<Texture> allTextures = getAll(Texture.class, new Array());

        final List portraits = Arrays.stream(allTextures.items)
                .filter(item -> item != null
                        && ((FileTextureData)item.getTextureData()).getFileHandle().file().getPath().contains("portrait\\"))
                .collect(Collectors.toList());

        return portraits;
    }

    public List<Texture> getAllTextures() {
        final Array<Texture> allTextures = getAll(Texture.class, new Array());

        final List portraits = Arrays.stream(allTextures.items)
                .filter(item -> item != null
                        && ((FileTextureData)item.getTextureData()).getFileHandle().file().getPath().contains(".png"))
                .collect(Collectors.toList());

        return portraits;
    }
}
