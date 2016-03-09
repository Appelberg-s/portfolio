#ifndef GAMEOBJECT_H_
#define GAMEOBJECT_H_

#include "GameEngine.h"

namespace engine {

class GameObject {
public:
    static std::shared_ptr<GameObject> getInstance(std::string path, GameEngine* win, int x, int y);
	virtual ~GameObject();
	virtual void tick() {}
	virtual void draw();

	SDL_Rect& getSrcRect(); 
	SDL_Rect& getDestRect();
	SDL_Surface* getSurface();
	SDL_Texture* getTexture();
	bool withinBounds(int x, int y) const;

	GameObject(const GameObject&) = delete;
	const GameObject& operator=(const GameObject&) = delete;

	virtual void collision(std::shared_ptr<GameObject> own,std::shared_ptr<GameObject> other){}
protected:
	GameObject(std::string path, GameEngine* win, int x, int y);
	GameEngine* window;
	SDL_Surface* surf;
	SDL_Texture* tx;
	SDL_Rect srcRect;
	SDL_Rect destRect;
};

} /* namespace engine */

#endif /* GAMEOBJECT_H_ */
