import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubRuleInListItem, getSubRuleInListItemIdentifier } from '../sub-rule-in-list-item.model';

export type EntityResponseType = HttpResponse<ISubRuleInListItem>;
export type EntityArrayResponseType = HttpResponse<ISubRuleInListItem[]>;

@Injectable({ providedIn: 'root' })
export class SubRuleInListItemService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-rule-in-list-items');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(subRuleInListItem: ISubRuleInListItem): Observable<EntityResponseType> {
    return this.http.post<ISubRuleInListItem>(this.resourceUrl, subRuleInListItem, { observe: 'response' });
  }

  update(subRuleInListItem: ISubRuleInListItem): Observable<EntityResponseType> {
    return this.http.put<ISubRuleInListItem>(
      `${this.resourceUrl}/${getSubRuleInListItemIdentifier(subRuleInListItem) as number}`,
      subRuleInListItem,
      { observe: 'response' }
    );
  }

  partialUpdate(subRuleInListItem: ISubRuleInListItem): Observable<EntityResponseType> {
    return this.http.patch<ISubRuleInListItem>(
      `${this.resourceUrl}/${getSubRuleInListItemIdentifier(subRuleInListItem) as number}`,
      subRuleInListItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubRuleInListItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubRuleInListItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubRuleInListItemToCollectionIfMissing(
    subRuleInListItemCollection: ISubRuleInListItem[],
    ...subRuleInListItemsToCheck: (ISubRuleInListItem | null | undefined)[]
  ): ISubRuleInListItem[] {
    const subRuleInListItems: ISubRuleInListItem[] = subRuleInListItemsToCheck.filter(isPresent);
    if (subRuleInListItems.length > 0) {
      const subRuleInListItemCollectionIdentifiers = subRuleInListItemCollection.map(
        subRuleInListItemItem => getSubRuleInListItemIdentifier(subRuleInListItemItem)!
      );
      const subRuleInListItemsToAdd = subRuleInListItems.filter(subRuleInListItemItem => {
        const subRuleInListItemIdentifier = getSubRuleInListItemIdentifier(subRuleInListItemItem);
        if (subRuleInListItemIdentifier == null || subRuleInListItemCollectionIdentifiers.includes(subRuleInListItemIdentifier)) {
          return false;
        }
        subRuleInListItemCollectionIdentifiers.push(subRuleInListItemIdentifier);
        return true;
      });
      return [...subRuleInListItemsToAdd, ...subRuleInListItemCollection];
    }
    return subRuleInListItemCollection;
  }
}
