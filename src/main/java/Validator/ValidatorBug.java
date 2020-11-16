package Validator;

import Domain.Bug;

public class ValidatorBug implements IValidator<Bug> {
    @Override
    public void validate(Bug entity) throws ValidationException {
        StringBuffer msg=new StringBuffer();
        if (entity.getDescription().length() < 10)
            msg.append("Please give a more detailed description!");
        if (msg.length()>0)
            throw new ValidationException(msg.toString());
    }
}
