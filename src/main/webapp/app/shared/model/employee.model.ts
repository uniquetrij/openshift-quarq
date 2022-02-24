import { IEmbedding } from 'app/shared/model/embedding.model';
import { IFile } from 'app/shared/model/file.model';

export interface IEmployee {
  id?: number;
  identifier?: string;
  name?: string;
  encryptionKey?: string;
  embeddings?: IEmbedding[];
  files?: IFile[];
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public identifier?: string,
    public name?: string,
    public encryptionKey?: string,
    public embeddings?: IEmbedding[],
    public files?: IFile[]
  ) {}
}
