#include "TextLabel.h"
#include "TTFManager.h"
#include <iostream>

namespace engine {

TextLabel::TextLabel(GameEngine* win, int x, int y, std::string text): MenuObject("images/ss1.png", win, x, y), text(text) {
	updateTexture();
}

std::shared_ptr<TextLabel> TextLabel::getInstance(GameEngine* win, int x, int y, std::string text) {
	return std::shared_ptr<TextLabel>(new TextLabel(win, x, y, text));
}

void TextLabel::setFontSize(int size) {
	fontSize = size;
	updateTexture();
}

void TextLabel::setColor(Uint8 r, Uint8 g, Uint8 b, Uint8 a) {
	_color = { r, g, b, a };
	updateTexture();
}

void TextLabel::draw() {
	SDL_RenderCopy(window->getRenderer(), textTexture, nullptr, &destRect);
}

std::string TextLabel::getText() const {
	return text;
}

TextLabel::~TextLabel() {
	SDL_DestroyTexture(textTexture);
}

void TextLabel::updateTexture() {
	if (textTexture != nullptr)
		SDL_DestroyTexture(textTexture);
	textTexture = TTFManager::getInstance().createFontTexture(window->getRenderer(), text.c_str(), fontSize, _color);
	TTFManager::getInstance().resizeRect(text, destRect);
}

} /* namespace engine */
