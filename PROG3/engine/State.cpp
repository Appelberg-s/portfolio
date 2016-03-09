#include "State.h"
#include "GameObject.h"
#include <algorithm>

namespace engine {

State::State(GameEngine* win): window(win) {
}

State::~State() {
}

void State::render() {
	for (auto obj : gameObjects) {
		obj->draw();
		obj->tick();
	}
}

void State::addGameObject(std::shared_ptr<GameObject> g) {
    gameObjects.push_back(g);
}

void State::removeGameObject(std::shared_ptr<GameObject> g) {
    gameObjects.erase(std::remove(gameObjects.begin(),gameObjects.end(),g),gameObjects.end());
}

} /* namespace engine */
