#include "InputHandler.h"

namespace engine {

InputHandler& InputHandler::getInstance() {
	static InputHandler handlerInstance;
	return handlerInstance;
}

bool InputHandler::isKeyDown(SDL_Keycode key) const {
	if (keyState != nullptr)
		if (keyState[SDL_GetScancodeFromKey(key)] == 1)
			return true;
	return false;
}

bool InputHandler::isMouseDown(MouseButton button) const {
	return mouseState[button];
}

std::string InputHandler::getCurrentChar() {
	edited = false;
	return currentChar;
}

int InputHandler::mouseX() {
	SDL_GetMouseState(&mX, &mY);
	return mX;
}

int InputHandler::mouseY() {
	SDL_GetMouseState(&mX, &mY);
	return mY;
}

void InputHandler::handleEvents() {
	SDL_Event event;
	while (SDL_PollEvent(&event)) {
		keyState = SDL_GetKeyboardState(nullptr);
		switch (event.type) {
		case SDL_QUIT:
			quit = true;
			break;
		case SDL_MOUSEBUTTONDOWN:
			onMouseDown(event);
			break;
		case SDL_MOUSEBUTTONUP:
			onMouseUp(event);
			break;
		case SDL_TEXTINPUT:
			onTextInput(event);
			break;
		}
	}
}

InputHandler::~InputHandler() {
}

void InputHandler::onMouseDown(SDL_Event& event) {
	if (event.button.button == SDL_BUTTON_LEFT)
		mouseState[LEFT_MOUSE] = true;
	if (event.button.button == SDL_BUTTON_RIGHT)
		mouseState[RIGHT_MOUSE] = true;
	if (event.button.button == SDL_BUTTON_MIDDLE)
		mouseState[MIDDLE_MOUSE] = true;
}

void InputHandler::onMouseUp(SDL_Event& event) {
	if (event.button.button == SDL_BUTTON_LEFT)
		mouseState[LEFT_MOUSE] = false;
	if (event.button.button == SDL_BUTTON_RIGHT)
		mouseState[RIGHT_MOUSE] = false;
	if (event.button.button == SDL_BUTTON_MIDDLE)
		mouseState[MIDDLE_MOUSE] = false;
}

void InputHandler::onTextInput(SDL_Event& event) {
	edited = true;
	currentChar = event.text.text;
}

} /* namespace engine */
