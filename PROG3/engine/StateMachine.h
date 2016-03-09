#ifndef STATEMACHINE_H_
#define STATEMACHINE_H_

#include <vector>

#include "State.h"

namespace engine {
class GameObject;
class StateMachine {
public:
	StateMachine();

	void pushState(std::shared_ptr<State> s);
	void changeState(std::shared_ptr<State> s);
	void popState();

	void update();
	void render();

	void removeGameObject(std::shared_ptr<GameObject> g);
private:
	std::vector<std::shared_ptr<State>> states;
};

} /* namespace engine */

#endif /* STATEMACHINE_H_ */
