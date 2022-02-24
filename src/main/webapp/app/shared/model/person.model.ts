import { Moment } from 'moment';

export interface IPerson {
  id?: number;
  name?: string;
  birth?: Moment;
  status?: string;
  country?: string;
  city?: string;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string,
    public birth?: Moment,
    public status?: string,
    public country?: string,
    public city?: string
  ) {}
}
