#ifndef MENUOBJECT_H_
#define MENUOBJECT_H_

#include "GameObject.h"

namespace engine {

class MenuObject: public GameObject {
public:
	MenuObject(std::string path, GameEngine* win, int x, int y);
};

} /* namespace engine */

#endif /* MENUOBJECT_H_ */
