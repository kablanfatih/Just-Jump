package com.kablanfatih.justjump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class JustJump extends ApplicationAdapter {

    SpriteBatch batch;
    Texture background;
    Texture mario;
    Texture enemy1;
    Texture enemy2;
    Texture enemy3;
    float marioX = 0;
    float marioY = 5;
    float [] mapX = new float[3];
    int gamestate = 0;
    float jump;
    float grawity = 0.6f;

   // ShapeRenderer shapeRenderer;
    int score = 0;
    int scoredEnemy = 0;
    BitmapFont font;
    BitmapFont font2;
    BitmapFont font3;

    Circle marioCircle;
    Circle[] enemyCircle;


    float enemyVelocity = 5;
    float distance = 0;
    Random random;


    int numberOfEnemies = 4;//Eğer ekran büyük olursa 4 adet düşman oluşturup döngüye girecek
    float[] enemyX = new float[numberOfEnemies];
    float enemyY = 5;
    float[] enemyOffSet = new float[numberOfEnemies];
    int result;

    private AssetManager manager;
    Sound jumpSound, roundEnd, deathSound;
    Music backgroundSound;

    public AssetManager getManager() {
        return manager;
    }


    @Override
    public void create() {



        //shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        background = new Texture("background.png");
        mario = new Texture("mario.png");
        marioX = Gdx.graphics.getWidth() / 3;
        marioY = 5;
        mapX[0] = 0;
        mapX[1] = 150;
        mapX[2] = 300;
        enemy1 = new Texture("enemy1.png");
        enemy2 = new Texture("enemy2.png");
        enemy3 = new Texture("enemy3.png");

        manager = new AssetManager();
        manager.load("background.ogg", Music.class);
        manager.load("jumpSound.ogg", Sound.class);
        manager.load("death.wav", Sound.class);
        manager.load("round_end.wav", Sound.class);
        manager.finishLoading();

        jumpSound = getManager().get("jumpSound.ogg");
        deathSound = getManager().get("death.wav");
        roundEnd = getManager().get("round_end.wav");
        backgroundSound = getManager().get("background.ogg");

        distance = Gdx.graphics.getWidth() / 2;
        random = new Random();

        marioCircle = new Circle();
        enemyCircle = new Circle[numberOfEnemies];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        font2 = new BitmapFont();
        font2.setColor(Color.RED);
        font2.getData().setScale(3);

        font3 = new BitmapFont();
        font3.setColor(Color.WHITE);
        font3.getData().setScale(2);


        for (int i = 0; i < numberOfEnemies; i++) {

            enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * distance;

            enemyCircle[i] = new Circle();

        }

    }

    @Override
    public void render() {

        batch.begin();
        mapRepeat();


        if (Gdx.input.justTouched()) {
            gamestate = 1;

        }

        if (gamestate == 1) {

            backgroundSound.play();
            backgroundSound.isLooping();

           // marioX++;

            if (enemyX[scoredEnemy] < marioX) {

                score++;

                if (scoredEnemy < numberOfEnemies - 1) {

                    scoredEnemy++;
                } else {

                    scoredEnemy = 0;
                }

                if (score >= 2 && score <= 4) {//Puan arttıkça hızlanmasını sağlayan if else bloğu

                    enemyVelocity = 7;

                } else if (score > 5 && score < 7) {

                    enemyVelocity = 10;
                    enemy1 = enemy2;

                } else if (score > 8 && score < 10) {

                    enemyVelocity = 11;


                } else if (score > 12 && score < 20) {
                    enemyVelocity = 12;
                    enemy1 = enemy3;


                } else if (score > 20)

                    enemyVelocity = 15;

            }

            result = score;

            for (int i = 0; i < numberOfEnemies; i++) {

                if (enemyX[i] < 0) {

                    enemyX[i] = enemyX[i] + numberOfEnemies * distance;


                    enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                } else {

                    enemyX[i] = enemyX[i] - enemyVelocity;
                }

                batch.draw(enemy1, enemyX[i], enemyY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

                enemyCircle[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, enemyY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
            }

            if (marioY >= 5 || jump < 0) {

                jump = jump + grawity;
                marioY = marioY - jump;
            }

            if (marioY <= 5) {
                if (Gdx.input.justTouched() /*&& marioY <= 5*/) {
                    jump = -14;
                    jumpSound.play();
                }
            }
            font.draw(batch, "Score: " + String.valueOf(score), Gdx.graphics.getWidth() / 18, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 20);
        } else if (gamestate == 0) {


        } else if (gamestate == 2) {

            backgroundSound.pause();
            //roundEnd.pause();

            font2.draw(batch, "Game Over! Tap To Play Again", Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
            font3.draw(batch, "Result Score: " + String.valueOf(result), Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 20);

            marioX = Gdx.graphics.getWidth() / 3;

            if (Gdx.input.justTouched()) {

                gamestate = 1;
            }
            for (int i = 0; i < numberOfEnemies; i++) {

                enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * distance; // düşmanlar arasındaki uzaklığı belirlemek için döngü oluşturuldu

                enemyCircle[i] = new Circle();
            }

            jump = 0;
            enemyVelocity = 5;
            score = 0;
            scoredEnemy = 0;
            enemy1 = new Texture("enemy1.png");


        }
        batch.draw(mario, marioX, marioY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 7);
        batch.end();

        marioCircle.set(marioX + Gdx.graphics.getWidth() / 30, marioY + Gdx.graphics.getHeight() / 15, Gdx.graphics.getWidth() / 30);

       /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(marioCircle.x, marioCircle.y, marioCircle.radius);*/


      /* for (int i = 0; i < numberOfEnemies; i++) {

            shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30, enemyY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);

        }
        shapeRenderer.end();*/

        for (int i = 0; i < numberOfEnemies; i++)

        {


            if (Intersector.overlaps(marioCircle, enemyCircle[i])) {

                roundEnd.play();
                gamestate = 2;
            }
        }

    }

    @Override
    public void dispose() {

    }

    public void mapRepeat() {

        batch.draw(background, mapX[0], 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

}


