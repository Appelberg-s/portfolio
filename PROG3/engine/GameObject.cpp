#include "GameObject.h"


namespace engine {

std::shared_ptr<GameObject> GameObject::getInstance(std::string path, GameEngine* win, int x, int y) {
    return std::shared_ptr<GameObject>(new GameObject(path,win,x,y));
}

GameObject::GameObject(std::string path, GameEngine* win, int x, int y): window(win){
	surf = IMG_Load(path.c_str());
	if (!surf)
		throw std::invalid_argument("Error at IMG_Load: " + std::string(SDL_GetError()));
	tx = SDL_CreateTextureFromSurface(window->getRenderer(),surf);
	if (!tx)
		throw std::invalid_argument("Error at SDL_CreateTextureFromSurface: " + std::string(SDL_GetError()));
	srcRect.w = surf->w;
	srcRect.h = surf->h;
	destRect.x = x;
	destRect.y = y;
	srcRect.x = 0;
	srcRect.y = 0;

	destRect.h = srcRect.h;
	destRect.w = srcRect.w;
}

void GameObject::draw() {
    SDL_RenderCopy(window->getRenderer(), tx, &srcRect, &destRect);
}

GameObject::~GameObject() {
	SDL_FreeSurface(surf);
	SDL_DestroyTexture(tx);
}

SDL_Rect& GameObject::getSrcRect() {
	return srcRect;
}

SDL_Rect& GameObject::getDestRect() {
	return destRect;
}

SDL_Surface* GameObject::getSurface() {
	return surf;
}

SDL_Texture* GameObject::getTexture() {
	return tx;
}

bool GameObject::withinBounds(int x, int y) const {
	return x <= destRect.x + destRect.w && x >= destRect.x &&
		y <= destRect.y + destRect.h && y >= destRect.y;
}

} /* namespace engine */
