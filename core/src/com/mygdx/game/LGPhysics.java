package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;

public class LGPhysics extends ApplicationAdapter {
	final float WORLD_WIDTH = 800f;
	final float WORLD_HEIGHT = 600f;
	final float TIME_STEP = 1/60f;
	final int VELOCITY_ITERATIONS = 6;
	final int POSITION_ITERATIONS = 2;

	private float accumulator;

	SpriteBatch batch;
	TextureAtlas textureAtlas;
	OrthographicCamera camera;
	ExtendViewport viewport;
	final HashMap<String, Sprite> sprites = new HashMap<>();
	World world;
	
	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, -10), true);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		batch = new SpriteBatch();
		textureAtlas = new TextureAtlas("sprites.txt");
		loadSprites();
	}

	private void loadSprites() {
		for (TextureAtlas.AtlasRegion region: textureAtlas.getRegions()) {
			sprites.put(region.name, textureAtlas.createSprite(region.name));
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.57f, 0.77f, 0.85f, 1);
		doPhysicsStep(Gdx.graphics.getDeltaTime());
		batch.begin();
		drawSprite(sprites.get("banana"), 0, 0);
		drawSprite(sprites.get("banana"), 130, 0);
		drawSprite(sprites.get("cherries"), 0, 130);
		batch.end();
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
	}

	private void drawSprite(Sprite sprite, float x, float y) {
		sprite.setPosition(x, y);
		sprite.draw(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
		textureAtlas.dispose();
		sprites.clear();
		world.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}
}
