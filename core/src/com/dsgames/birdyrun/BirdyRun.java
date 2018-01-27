package com.dsgames.birdyrun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;



public class BirdyRun extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;


	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle pipeRect1;
	Rectangle pipeRect2;
	Texture[] birds=new Texture[2];
	int flagstate=0;
	float birdY=0;
	float velocity=0;

	int gameState=0;
	float gravity=2;
	int numberOfTubes=4;
	float[] gap=new float[numberOfTubes];

	float[] tubeX=new float[numberOfTubes];
	float tubeVelocity=4;

	Texture bottomtube;
	Texture toptube;

	Random random=new Random();


	float distance;

	int score=0;
	int scoringTube=0;
	BitmapFont font;

	Texture gameover;

	
	@Override
	public void create () {



		batch = new SpriteBatch();
		background= new Texture("bg.jpg");

		//shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();
		pipeRect1=new Rectangle();
		pipeRect2=new Rectangle();

		font=new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(4);


		birds[0]= new Texture("bird.png");
		birds[1]=new Texture("bird2.png");


        bottomtube=new Texture("bottomtube.png");

		toptube=new Texture("toptube.png");
		gameover=new Texture("gameover.png");
		distance=Gdx.graphics.getWidth()* 3/4;

		gameStart();



	}




	public void gameStart()
	{
		birdY=Gdx.graphics.getHeight()/2-birds[flagstate].getHeight()/2;
		for( int i=0;i<numberOfTubes;i++)
		{
			gap[i]=(random.nextFloat())*450f+480f;
			tubeX[i]=Gdx.graphics.getWidth()/2-bottomtube.getWidth()/2 + Gdx.graphics.getWidth()+ (i*distance);
		}
	}


	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);






		if(gameState==1)
		{


			if(tubeX[scoringTube]<(Gdx.graphics.getWidth()/2)) {
				if (scoringTube < numberOfTubes-1) {
					score=score+50;
					scoringTube++;
				}
				else
					scoringTube=0;
			}

			if(Gdx.input.justTouched())
			{
				velocity=-30;



			}


			for( int i=0;i<numberOfTubes;i++) {

				if(tubeX[i]<-toptube.getWidth())
				{
					tubeX[i]+=distance*numberOfTubes;


						gap[i]=(random.nextFloat())*450f+480f;



				}

				else
				{
					tubeX[i]=tubeX[i]-tubeVelocity;
					score++;
				}

				font.draw(batch, String.valueOf(score), 100,200);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap[i]/ 2 - bottomtube.getHeight());
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap[i]/ 2);


				pipeRect1.set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap[i]/ 2, toptube.getWidth(), toptube.getHeight());
				pipeRect2.set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap[i]/ 2 - bottomtube.getHeight(), bottomtube.getWidth(), bottomtube.getHeight());

				if (Intersector.overlaps(birdCircle, pipeRect1) || (Intersector.overlaps(birdCircle, pipeRect2))) {
					gameState=2;
				}

				//shapeRenderer.setColor(Color.YELLOW);
				//shapeRenderer.rect(pipeRect1.x, pipeRect1.y, pipeRect1.width,pipeRect1.height);
				//shapeRenderer.rect(pipeRect2.x, pipeRect2.y, pipeRect2.width,pipeRect2.height);
			}

			if(birdY>0 && (birdY+birds[flagstate].getHeight()<Gdx.graphics.getHeight() ))
			{
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}
			else
			{
				gameState=2;
			}

		}
		else if(gameState==0)
		{
			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}

		else
		{
			batch.draw(gameover, Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			font.draw(batch, "Score:"+ String.valueOf(score), Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/8 );
			if(Gdx.input.justTouched()) {
				gameState = 0;
				gameStart();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}

		}

		if(flagstate==0)
		{
			flagstate=1;

		}

		else
		{
			flagstate=0;
		}
		batch.draw(birds[flagstate], Gdx.graphics.getWidth()/2-birds[flagstate].getWidth()/2, birdY  );


		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[flagstate].getHeight()/2, birds[flagstate].getWidth()/2);


		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		//shapeRenderer.end();




	}




}

