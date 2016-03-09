#ifndef INPUTHANDLER_H_
#define INPUTHANDLER_H_

#include <SDL.h>
#include <string>

namespace engine {
	
enum MouseButton {
	LEFT_MOUSE = 0,
	RIGHT_MOUSE = 1,
	MIDDLE_MOUSE = 2
};

class InputHandler {
public:
	static InputHandler& getInstance();

	bool isKeyDown(SDL_Keycode key) const;
	bool isMouseDown(MouseButton button) const;
	bool isEdited() const { return edited; }
	bool isExit() const { return quit; }
	std::string getCurrentChar();
	int mouseX();
	int mouseY();
	void handleEvents();

	~InputHandler();
	InputHandler(const InputHandler&) = delete;
	InputHandler& operator=(const InputHandler&) = delete;
private:
	bool quit = false;
	InputHandler() {
		for (int i = 0; i < 3; i++)
			mouseState[i] = false;
	};

	const Uint8* keyState = SDL_GetKeyboardState(nullptr);
	bool mouseState[3];
	int mX = 0;
	int mY = 0;
	bool edited = false;
	std::string currentChar;
	void onMouseDown(SDL_Event&);
	void onMouseUp(SDL_Event&);
	void onTextInput(SDL_Event&);
};

} /* namespace engine */

#endif /* INPUTHANDLER_H_ */
