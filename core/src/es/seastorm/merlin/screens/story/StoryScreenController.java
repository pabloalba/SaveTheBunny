package es.seastorm.merlin.screens.story;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import dragongames.base.controller.Controller;
import dragongames.base.gameobject.AbstractGameObject;
import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.Constants;
import es.seastorm.merlin.MerlinGame;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;
import es.seastorm.merlin.screens.story.level.LevelItem;
import es.seastorm.merlin.screens.story.level.PhaseItem;


public class StoryScreenController extends Controller {
    public static final int MODE_PHASE = 0;
    public static final int MODE_LEVEL = 1;


    public Array<AbstractGameObject> btnLevels;
    AbstractGameObject bg, arrowLeft, arrowRight;
    PhaseItem phase1, phase2, phase3, phase4;
    int mode = MODE_PHASE;
    int firstLevel = 0;
    int phaseNum;


    public StoryScreenController(Game game) {
        super(game, Constants.WIDTH, Constants.HEIGHT);
        Gdx.input.setCatchBackKey(true);
    }

    public void update(float deltaTime) {
        //Nothing
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (mode == MODE_PHASE) {
                ((MerlinGame) game).loadMenuScreen();
            } else {
                showSelectPhase();
            }
        }
        return false;
    }

    private void createGameObjects() {
        //Background
        addGameObject(Cache.backgroundMenu);

        int stars = 0;
        int maxStars = 0;

        for (int i = 0; i < Constants.LEVEL_LIST[0].length; i++) {
            stars += ((MerlinGame) game).getStars(0, i);
            maxStars += 3;
        }


        phase1 = new PhaseItem(GameAssets.instance.getTextureRegion(GameAssets.BTN_PHASE1), 1, stars + "/" + maxStars, "A dog's life", 150, 400);
        addGameObject(phase1);

        stars = 0;
        maxStars = 0;
        for (int i = 0; i < Constants.LEVEL_LIST[1].length; i++) {
            stars += ((MerlinGame) game).getStars(1, i);
            maxStars += 3;
        }

        phase2 = new PhaseItem(GameAssets.instance.getTextureRegion(GameAssets.BTN_PHASE2), 2, stars + "/" + maxStars, "Foxy time", 755, 400);
        addGameObject(phase2);

        stars = 0;
        maxStars = 0;
        for (int i = 0; i < Constants.LEVEL_LIST[2].length; i++) {
            stars += ((MerlinGame) game).getStars(2, i);
            maxStars += 3;
        }


        phase3 = new PhaseItem(GameAssets.instance.getTextureRegion(GameAssets.BTN_PHASE3), 3, stars + "/" + maxStars, "Double it", 150, 20);
        addGameObject(phase3);

        stars = 0;
        maxStars = 0;
        for (int i = 0; i < Constants.LEVEL_LIST[3].length; i++) {
            stars += ((MerlinGame) game).getStars(3, i);
            maxStars += 3;
        }

        phase4 = new PhaseItem(GameAssets.instance.getTextureRegion(GameAssets.BTN_PHASE4), 4, stars + "/" + maxStars, "All in", 755, 20);
        addGameObject(phase4);

        createButtons();


    }

    private void createButtons() {
        btnLevels = new Array<AbstractGameObject>(20);
        bg = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_LEVEL_SELECT_BG));
        bg.position.x = posMiddleX(bg);
        bg.position.y = posMiddleY(bg);
        bg.visible = false;
        addGameObject(bg);


        int row = 0;
        int column = 3;

        AbstractGameObject btn = new SimpleGameObject(LevelItem.lockRegion);

        //float gapX = ((bg.dimension.x - 240) - 6 * btn.dimension.x) / 8;
        //float gapY = ((bg.dimension.y - 240) - 3 * btn.dimension.y) / 4;
        float gapX = 15.125f;
        float gapY = 9.5f;


        for (int i = 0; i < 18; i++) {
            float posX = 120 + bg.position.x + ((row + 1) * gapX) + ((row) * btn.dimension.x);
            float posY = (bg.position.y - 40) + ((column + 1) * gapY) + ((column) * btn.dimension.y);


            btn = new LevelItem(false, i, "", posX, posY);


            btn.visible = false;

            addGameObject(btn);
            btnLevels.add(btn);
            row++;
            if (row > 5) {
                row = 0;
                column--;
            }
        }

        arrowLeft = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ARROW_LEFT));
        arrowRight = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ARROW_RIGHT));
        arrowLeft.position.x = 160;
        arrowLeft.position.y = posMiddleY(arrowLeft);
        arrowRight.position.x = 1030;
        arrowRight.position.y = posMiddleY(arrowLeft);
        arrowLeft.visible = false;
        arrowRight.visible = false;
        addGameObject(arrowLeft);
        addGameObject(arrowRight);
    }


    @Override
    protected void init() {
        createGameObjects();
        mode = MODE_PHASE;
    }

    public void touch(float x, float y) {
        Array<AbstractGameObject> list = touchedGameObjects(x, y);
        if (mode == MODE_PHASE) {

            for (AbstractGameObject btn : list) {
                if (phase1 == btn) {
                    selectPhase(0);
                } else if (phase2 == btn) {
                    selectPhase(1);
                } else if (phase3 == btn) {
                    selectPhase(2);
                } else if (phase4 == btn) {
                    selectPhase(3);
                }
            }


        } else if (mode == MODE_LEVEL) {
            for (AbstractGameObject btn : list) {
                if (btnLevels.contains(btn, true)) {
                    loadStory(((LevelItem) btn).levelNum);
                } else if (arrowLeft == btn) {
                    move(-18);
                } else if (arrowRight == btn) {
                    move(18);
                }

            }
        }
    }

    private void move(int desp) {
        firstLevel += desp;
        updateButtons();
        showArrows();
    }

    private void selectPhase(int num) {
        phase1.visible = false;
        phase2.visible = false;
        phase3.visible = false;
        phase4.visible = false;
        firstLevel = 0;
        phaseNum = num;

        updateButtons();
        showArrows();

        bg.visible = true;

        for (AbstractGameObject btn : btnLevels) {
            btn.visible = true;
        }


        mode = MODE_LEVEL;


    }

    private void showArrows() {
        arrowLeft.visible = false;
        arrowRight.visible = false;

        if (firstLevel > 17) {
            arrowLeft.visible = true;
        }

        if (firstLevel + 19 < Constants.LEVEL_LIST[phaseNum].length) {
            arrowRight.visible = true;
        }
    }

    private void showSelectPhase() {

        bg.visible = false;

        for (AbstractGameObject btn : btnLevels) {
            btn.visible = false;
        }

        phase1.visible = true;
        phase2.visible = true;
        phase3.visible = true;
        phase4.visible = true;


        mode = MODE_PHASE;

    }

    private void loadStory(int story) {
        System.out.println("Load Story: " + story);
        if ((story > -1) && (((MerlinGame) game).maxLevel[phaseNum] >= story)) {
            ((MerlinGame) game).loadLabyrintScreen(phaseNum, story);
        }
    }

    private void updateButtons() {
        int i = firstLevel;
        for (AbstractGameObject btn : btnLevels) {
            int stars = ((MerlinGame) game).getStars(phaseNum, i);
            ((LevelItem) btn).setInfo(i, Integer.toString(i + 1), stars, i > ((MerlinGame) game).maxLevel[phaseNum]);
            i++;
        }


    }


}

