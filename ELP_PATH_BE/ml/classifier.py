import os
import sys
import json
import pandas as pd

# Get the directory of the script
script_dir = os.path.dirname(os.path.realpath(__file__))

# Specify the relative path to the model file
model_file_path = os.path.join(script_dir, 'model.pkl')

# Load the model and vectorizer
model, vectorizer = pd.read_pickle(model_file_path)

def transform(r):
    return True if r else False

def classify(text):
    t = vectorizer.transform(text)
    result = model.predict(t).tolist()[0]
    result = [transform(x) for x in result]
    result_json = json.dumps({
        'toxic': result[0],
        'severe_toxic': result[1],
        'obscene': result[2],
        'threat': result[3],
        'insult': result[4],
        'identity_hate': result[5]
    })

    print(result_json)

# Check for command-line arguments
if len(sys.argv) < 2:
    print("Please provide some text as a command-line argument")
    sys.exit(1)

classify(sys.argv[1:])
