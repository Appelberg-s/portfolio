#ifndef GAMEENGINE_H_
#define GAMEENGINE_H_

#include <SDL.h> 
#include <SDL_image.h>
#include <SDL_ttf.h>

#include <string>
#include <memory>
#include "StateMachine.h"

namespace engine {
	
class GameObject;
class StateMachine;
class Sprite;

class GameEngine {
public:
	~GameEngine();
	static const int POS_CENTERED = SDL_WINDOWPOS_CENTERED;
	static GameEngine* initialize(std::string s, int x, int y, int w, int h, int fps);
	void run();
	void setFPS(int fps);
	void addState(std::shared_ptr<State>);
	StateMachine& getStateMachine();
	bool repeatCheck();

	int getWidth() const;
	int getHeight() const;

	SDL_Renderer* getRenderer() const;
	SDL_Window* getWindow() const;
private:
	GameEngine(std::string s, int x, int y, int w, int h, int f);
	SDL_Window* window;
	SDL_Renderer* renderer;
	int fps;
	StateMachine stateMachine;

	int oldTime = 0;
	int newTime = SDL_GetTicks();
	int repeats = 0;

	int width;
	int height;

	GameEngine(const GameEngine& g) = delete;
	const GameEngine& operator=(GameEngine* g) = delete;
};
} /* namespace engine */

#endif /* GAMEENGINE_H_ */
