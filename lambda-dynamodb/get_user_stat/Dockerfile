FROM public.ecr.aws/lambda/python:3.8

# Copy function code
COPY get_user_stat.py ${LAMBDA_TASK_ROOT}/app.py

# Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
CMD [ "app.handler" ]