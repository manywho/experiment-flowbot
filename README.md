Flowbot
=======

> This experiment is currently in development, and not yet recommended for use in production environments

[![Build Status](https://travis-ci.org/manywho/experiment-flowbot.svg)](https://travis-ci.org/manywho/experiment-flowbot)

This experiment is a Slack bot that allows you to run your Boomi Flow apps inside of Slack, using slash commands and 
interactive messages.

It makes some assumptions about flow responses, like:

* If a flow returns a page element, with a single input and single outcome, the input isn't shown and the user's reply is sent as the value, etc.

## Running

The bot is compatible with Heroku, and can be deployed by clicking the button below:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/manywho/experiment-flowbot)

## Contributing

Contributions are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](https://opensource.org/licenses/MIT).
