#ifndef TEXTFIELD_H_
#define TEXTFIELD_H_

#include "TextLabel.h"

namespace engine {

class TextField :public TextLabel {
public:
	static std::shared_ptr<TextField> getInstance(GameEngine* win, int x, int y);
	void draw() override;
	void handleKeyBoard();
	void setBorder(bool b) { border = b; }
protected:
	TextField(GameEngine* win, int x, int y);
private:
	bool border = true;
	bool focused = false;
};

}

#endif
