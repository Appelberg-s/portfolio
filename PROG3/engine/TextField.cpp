#include "TextField.h"
#include "InputHandler.h"

namespace engine {

TextField::TextField(GameEngine* win, int x, int y): TextLabel(win, x, y, "     ") {
}

std::shared_ptr<TextField> TextField::getInstance(GameEngine* win, int x, int y) {
	return std::shared_ptr<TextField>(new TextField(win, x, y));
}

void TextField::draw() {
	if (InputHandler::getInstance().isMouseDown(LEFT_MOUSE))
		if (withinBounds(InputHandler::getInstance().mouseX(), InputHandler::getInstance().mouseY()))
			SDL_StartTextInput();
		else 
			SDL_StopTextInput();
	if (InputHandler::getInstance().isKeyDown(SDLK_BACKSPACE) && window->repeatCheck()) {
		text = text.substr(0, text.size() - 1);
		updateTexture();
	}
	if (InputHandler::getInstance().isEdited())
		handleKeyBoard();
	if (border) {
		SDL_SetRenderDrawColor(window->getRenderer(), 0xFF, 0xFF, 0xFF, 0xFF);
		SDL_RenderDrawRect(window->getRenderer(), &destRect);
		SDL_SetRenderDrawColor(window->getRenderer(), 0, 0, 0, 0xFF);
	}
	SDL_RenderCopy(window->getRenderer(), textTexture, nullptr, &destRect);
}

void TextField::handleKeyBoard() {
	text += InputHandler::getInstance().getCurrentChar();
	updateTexture();
}

}
