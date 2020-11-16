package Validator;

import Domain.Programmer;

public class ValidatorProgrammer implements IValidator<Programmer> {
    @Override
    public void validate(Programmer entity) throws ValidationException {
        StringBuffer msg=new StringBuffer();
        if (entity.getSalary() < 3000)
            msg.append("A programmer cannot have a salary lower than 3000 M.U.");
        if (msg.length()>0)
            throw new ValidationException(msg.toString());
    }
}
