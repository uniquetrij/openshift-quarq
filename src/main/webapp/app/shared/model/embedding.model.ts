import { IEmployee } from 'app/shared/model/employee.model';

export interface IEmbedding {
  id?: number;
  embeddingContentType?: string;
  embedding?: any;
  employees?: IEmployee[];
}

export class Embedding implements IEmbedding {
  constructor(public id?: number, public embeddingContentType?: string, public embedding?: any, public employees?: IEmployee[]) {}
}
