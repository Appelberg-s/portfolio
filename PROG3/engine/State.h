#ifndef STATE_H_
#define STATE_H_
#include <vector>
#include <memory>

namespace engine {

class GameEngine;
class GameObject;

class State {
public:
	virtual ~State();

	virtual void update() = 0;
	virtual void render();

	State(const State&) = delete;
	const State& operator=(const State&) = delete;

    void addGameObject(std::shared_ptr<GameObject> g);
    virtual void removeGameObject(std::shared_ptr<GameObject> g);
protected:
	State(GameEngine*);
	std::vector<std::shared_ptr<GameObject>> gameObjects;
	GameEngine* window;
};

} /* namespace engine */

#endif /* STATE_H_ */
