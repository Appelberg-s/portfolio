#include "StateMachine.h"
#include "GameObject.h"

namespace engine {

StateMachine::StateMachine() {
}

void StateMachine::pushState(std::shared_ptr<State> s) {
    states.push_back(s);
}

void StateMachine::changeState(std::shared_ptr<State> s) {
    popState();
    pushState(s);
}

void StateMachine::popState() {
    if (!states.empty()) {
        states.back().reset();
        states.pop_back();
    }
}

void StateMachine::update() {
    if (!states.empty())
        states.back()->update();
}

void StateMachine::render() {
    if (!states.empty())
        states.back()->render();
}

void StateMachine::removeGameObject(std::shared_ptr<GameObject> g) {
    states.back()->removeGameObject(g);
}

} /* namespace engine */
