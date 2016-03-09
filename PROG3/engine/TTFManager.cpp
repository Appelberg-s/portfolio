#include "TTFManager.h"
#include <stdexcept>

namespace engine {

TTFManager& TTFManager::getInstance() {
	static TTFManager managerInstance;
	return managerInstance;
}

void TTFManager::setFont(std::string fontname) {
	_fontName = fontname;
	TTF_CloseFont(currentFont);
	openFont(_fontName);
}

void TTFManager::setFontSize(int size) {
	_size = size;
	setFont(_fontName);
}

TTF_Font* TTFManager::getFont() const {
	return currentFont;
}

void TTFManager::reset() {
	setFontSize(DEFAULT_SIZE);
	setFont(DEFAULT_FONT);
}

void TTFManager::resizeRect(std::string text, SDL_Rect& destRect) const {
	TTF_SizeText(currentFont, text.c_str(), &destRect.w, &destRect.h);
}

SDL_Texture* TTFManager::createFontTexture(SDL_Renderer* ren, std::string text, int size, SDL_Color color) {
	setFontSize(size);
	SDL_Surface* textSurface = TTF_RenderUTF8_Blended(currentFont, text.c_str(), color);
	SDL_Texture* textTexture = SDL_CreateTextureFromSurface(ren, textSurface);
	SDL_FreeSurface(textSurface);
	return textTexture;
}

TTFManager::~TTFManager() {
	TTF_CloseFont(currentFont);
	TTF_Quit();
}

void TTFManager::openFont(std::string fontName) {
	currentFont = TTF_OpenFont((FONT_PATH + fontName).c_str(), _size);
	if (currentFont == nullptr)
		throw std::logic_error("Error at TTF_OpenFont: "+ std::string(TTF_GetError()));
}

TTFManager::TTFManager() {
	if (TTF_Init() == -1)
		throw std::logic_error("Error at TTF_Init: " + std::string(TTF_GetError()));
	openFont(_fontName);
}

}
