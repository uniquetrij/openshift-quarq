import { IEmployee } from 'app/shared/model/employee.model';
import { EncryptionAlgorithm } from 'app/shared/model/enumerations/encryption-algorithm.model';

export interface IFile {
  id?: number;
  identifier?: string;
  location?: string;
  uri?: string;
  encryption?: boolean;
  encryptionAlgorithm?: EncryptionAlgorithm;
  employees?: IEmployee[];
}

export class File implements IFile {
  constructor(
    public id?: number,
    public identifier?: string,
    public location?: string,
    public uri?: string,
    public encryption?: boolean,
    public encryptionAlgorithm?: EncryptionAlgorithm,
    public employees?: IEmployee[]
  ) {
    this.encryption = this.encryption || false;
  }
}
