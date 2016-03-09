#include "GameEngine.h"
#include "StateMachine.h"
#include "InputHandler.h"

#include <iostream>

namespace engine {

GameEngine::GameEngine(std::string s, int x, int y, int w, int h, int f): window{SDL_CreateWindow(s.c_str(),x,y,w,h,0)}, renderer(SDL_CreateRenderer(window,-1,0)), fps(f), width(w), height(h){
	if (!window)
		throw std::logic_error("Error at SDL_CreateWindow: " + std::string(SDL_GetError()));
	if (!renderer)
		throw std::logic_error("Error at SDL_CreateRenderer: " + std::string(SDL_GetError()));
}

GameEngine::~GameEngine() {
	SDL_DestroyRenderer(renderer);
	SDL_DestroyWindow(window);
	SDL_Quit();
}

GameEngine* GameEngine::initialize(std::string s, int x, int y, int w, int h, int f) {
	return new GameEngine(s,x,y,w,h,f);
}

int GameEngine::getWidth() const {
    return width;
}
int GameEngine::getHeight() const {
    return height;
}

void GameEngine::run() {
	bool running = true;
	Uint32 nextFrame;
	int interval = 1000/fps;

	while (running) {
		nextFrame = SDL_GetTicks() + interval;

		InputHandler::getInstance().handleEvents();
		if (InputHandler::getInstance().isExit())
			running = false;

		SDL_RenderClear(renderer);

		stateMachine.update();
		stateMachine.render();

		SDL_RenderPresent(renderer);

		if (!SDL_TICKS_PASSED(SDL_GetTicks(), nextFrame))
			SDL_Delay(nextFrame - SDL_GetTicks());
	}
}

void GameEngine::setFPS(int fps) {
	this->fps = fps;
}

void GameEngine::addState(std::shared_ptr<State> state) {
	stateMachine.pushState(state);
}

StateMachine& GameEngine::getStateMachine() {
	return stateMachine;
}

bool GameEngine::repeatCheck() {
    oldTime = newTime;
    newTime = SDL_GetTicks();
    if (repeats > 15 || newTime - oldTime > 20) {
        repeats = 0;
        return true;
    }
    repeats++;
    return false;
}

SDL_Renderer* GameEngine::getRenderer() const {
	return renderer;
}

SDL_Window* GameEngine::getWindow() const {
	return window;
}

} /* namespace engine */
