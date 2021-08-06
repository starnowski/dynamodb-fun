from app import app


def main():
    app.run(host='0.0.0.0', debug=True, threaded=True)


if __name__ == "__main__":
    main()
