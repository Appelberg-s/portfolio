# Skriven med 40 minuters erfarenhet av python
def safe(*vec):
    if len(vec) == 1:
        try: 
            exec(vec[0])
        except Exception as e:
            print(e)
    else:
        assignment = vec[0][0] + " = " + vec[0][1]
        exec(assignment)
        try: 
            exec(vec[1])
        except Exception as e:
            print(e)
        finally:
            if hasattr(eval(vec[0][0]),"close"):
                eval(vec[0][0]).close()

safe("print(10/2)")
safe(["s", "open('file.txt', 'r')"], "print(ord(s.read(1)))")

# Skillnaden är alltså att man gör quotes vid anrop
# istället för inne i funktionen
